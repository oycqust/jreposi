package com.utstar.integral.service.impl;

import ch.qos.logback.classic.Logger;
import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.ItemBean;
import com.utstar.integral.bean.UserViewLog;
import com.utstar.integral.log.LoggerBuilder;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.service.ActivityService;
import com.utstar.integral.service.CalculateService;
import com.utstar.integral.thread.ThreadPoolManage;
import com.utstar.integral.type.ActivityDataType;
import com.utstar.integral.type.ActivityPointDelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * created by UTSC1244 at 2019/5/17 0017
 */
@Service
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    @Resource
    private RedisCommonDAO redisCommonDAO;
    @Resource
    private ActivityService activityService;
    @Resource
    private ThreadPoolManage threadPoolManage;

    @Override
    public boolean handleUserViewLogs(List<UserViewLog> userViewLogs) {
        if (CollectionUtils.isEmpty(userViewLogs)) {
            log.info("activity: handle viewlogs is empty. handle is over");
            return false;
        }

        log.info("activity: handle viewlogs start, viewLogs size is {}",
                userViewLogs.size());
        //获取所有没有失效的活动
        List<ActivityEntity> activityEntities = activityService.findValidActivities();
        if (CollectionUtils.isEmpty(activityEntities)) {
            log.info("activity: query valid activity from db is empty.");
            return false;
        }
        CountDownLatch countDownLatch = new CountDownLatch(activityEntities.size());
        for (ActivityEntity activityEntity : activityEntities) {
            handleActiByThread(userViewLogs, countDownLatch, activityEntity, true);
        }
        try {
            countDownLatch.await();
            if(!ThreadPoolManage.cacheList.isEmpty()){
                CountDownLatch cacheCountDown = new CountDownLatch(ThreadPoolManage.cacheList.size());
                for (ActivityEntity activityEntity : ThreadPoolManage.cacheList) {
                    handleActiByThread(userViewLogs, cacheCountDown, activityEntity, false);
                    cacheCountDown.await();
                    ThreadPoolManage.cacheList.clear();
                }
            }
        } catch (InterruptedException e) {
            log.error("fail to thread countdown await", e);
        }

        return true;
    }

    private void handleActiByThread(List<UserViewLog> userViewLogs, CountDownLatch countDownLatch,
                                    ActivityEntity activityEntity, boolean isCache) {
        try {
            threadPoolManage.getThreadPoolExecutor().submit(() ->{
                handleActi(userViewLogs, activityEntity);
                countDownLatch.countDown();
            });
        } catch (RejectedExecutionException e) {
            log.error("thread pool is busy", e);
            if(isCache){
                ThreadPoolManage.cacheList.add(activityEntity);
            }
        }
    }

    private void handleActi(List<UserViewLog> userViewLogs, ActivityEntity activityEntity) {
        //存放计算的用户积分map key:redis中的key
        Map<String, Long> upMap = new LinkedHashMap<>();
        //存放计算的用户观看次数map key:redis中的key
        Map<String, Long> mvMap = new HashMap<>();

        //取出配置的媒资code
        Map<String, Set<String>> codeMap = getConfigCode(activityEntity, activityEntity.getSysId());

        if (CollectionUtils.isEmpty(codeMap) && !(activityEntity.getDataType().equals(ActivityDataType.TYPE)
                &&activityEntity.getMediaCode().equals("2"))) {
            log.error("activity: mediacode the user config is empty. the activity is {}", activityEntity);
            return;
        }
        //处理viewlog
        for (UserViewLog userViewLog : userViewLogs) {
            String viewSysid = userViewLog.getSysid();
            //TODO TEST
            if(userViewLog.getType().equals("c")&&activityEntity.getDataType().equals(ActivityDataType.TYPE)
                    &&activityEntity.getMediaCode().contains("2")){
                System.out.println("2");
            }
            //校验运营商
            if (StringUtils.isBlank(viewSysid)
                    || !activityEntity.getSysId().contains(viewSysid)) {
                continue;
            }
            //校验mediacode
            if (!validateViewCode(activityEntity, activityEntity.getDuration(), codeMap, userViewLog)){
                continue;
            }

            //用户积分key up_${activityid}_${userid}_${sysid}
            String upkey = String.format(Constant.USER_POINT_FORMAT, activityEntity.getActivityId(), userViewLog.getUserid(), userViewLog.getSysid());
            //媒资观看次数 mv_${activityid}_${mediacode}_${sysid}
            String mvkey = String.format(Constant.MOVIE_VIEW_FORMAT, activityEntity.getActivityId(), userViewLog.getMediacode(), userViewLog.getSysid());
            switch (activityEntity.getZeroDel()) {
                case DEL_DAY://每日清零
                    //设置失效时间
                    if(!setExpiredTime(upkey, mvkey)){
                        return;
                    }
                    break;
                case NOT_DEL://持续累积（不清零）
                    break;
            }
            //计算积分
            calculatePoint(upMap, upkey, activityEntity.getPoint(), activityEntity.getLimit());
            //计算观看次数
            calculateViewCount(mvMap, mvkey, userViewLog.getSecond());
        }
        //1个活动计算结束，将计算结果存入redis
        //LoggerFactory.getLogger(activityEntity.getActivityId()) TODO basePath从activityEntity中取
        Logger logger = LoggerBuilder.getLogger(activityEntity.getActivityId());
        store2RedisAndlog(activityEntity.getActivityId(), upMap, mvMap);
    }

    //从redis中查找对应活动的mediacode
    private Map<String, Set<String>> getConfigCode(ActivityEntity activityEntity, String sysIdStr) {

        String mediaType = activityEntity.getMediaType();
        Map<String, Set<String>> codeMap = new HashMap<>();
        /**
         * 存放用户配置用于需要统计积分的媒资code
         * key:activity_${mediatype}_${activityid}_${duration}_${point}_${isZeroDel}_${limit}_${sysid}
         */
        try {
            for (String sysid : sysIdStr.split(",")) {
                String key = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT,
                        mediaType, activityEntity.getActivityId(),
                        activityEntity.getDuration().toString(), String.valueOf(activityEntity.getPoint()),
                        activityEntity.getZeroDel().ordinal(), activityEntity.getLimit(),sysid);
                log.info("activity: the redis key is {}, activity is {}", key, activityEntity);
                Set<String> mediacodeStrSet = redisCommonDAO.sMembers(key);
                if (CollectionUtils.isEmpty(mediacodeStrSet)) continue;
                codeMap.put(sysid, mediacodeStrSet);
            }
        } catch (Exception e) {
            String msg = String.format("fail to find mediacode from redis. activity is %s", activityEntity.toString());
            log.error(msg, e);
        }
        return codeMap;
    }

    //校验mediacode  c直播 p不处理 v点播
    private boolean validateViewCode(ActivityEntity activityEntity, Integer duration,
                                     Map<String, Set<String>> codeMap, UserViewLog userViewLog) {

        if(StringUtils.isBlank(userViewLog.getType())
                || "p".equals(userViewLog.getType())){
            return false;
        }
        if (activityEntity.getDataType().equals(ActivityDataType.TYPE)) {
            //type处理
            String mediacodeStr = activityEntity.getMediaCode();
            if(!poccessType(codeMap, userViewLog, mediacodeStr)){
                return false;
            }
        }else{
            //校验mideacode是否存在
            if (!validateMediaCodeIsExists(codeMap, userViewLog.getSysid(), userViewLog.getMediacode())) {
                return false;
            }
        }

        //view时间是否大于配置时间
        long userViewTimeMills = userViewLog.getViewEnd().getTime() - userViewLog.getViewStart().getTime();
        if (userViewTimeMills < duration * 1000) {//观看时长小于用户设置的最小时长
            if (log.isDebugEnabled()) {
                log.debug("activity: the viewTimeMills[{}] of media is less than durationMills[{}] user set",
                        userViewTimeMills, duration * 1000);
            }
            return false;
        }
        return true;
    }

    private boolean poccessType(Map<String, Set<String>> codeMap, UserViewLog userViewLog, String mediacodeStr) {
        if (mediacodeStr.contains("2"))//包含直播
        {
            if ("c".equals(userViewLog.getType())) {//是否是直播
                return true;
            }
            if (mediacodeStr.contains("0") || mediacodeStr.contains("1")) {//包含点播
                if ("v".equals(userViewLog.getType())){
                    return validateMediaCodeIsExists(codeMap, userViewLog.getSysid(), userViewLog.getMediacode());
                }
            }
        } else if (!mediacodeStr.contains("2")) {//点播
            if ("v".equals(userViewLog.getType())){
                return validateMediaCodeIsExists(codeMap, userViewLog.getSysid(), userViewLog.getMediacode());
            }
        }
        return false;
    }

    /**
     * 设置key当日过期
     *
     * @param keys 多个key
     */
    private boolean setExpiredTime(String... keys) {
        for (String key : keys) {
            if(!setExpiredTime(key)){
                return false;
            }
        }
        return true;
    }

    private boolean validateMediaCodeIsExists(Map<String, Set<String>> codeMap, String viewSysid, String mediacode) {
        Set<String> mediacodeStrSet = codeMap.get(viewSysid);
        if (CollectionUtils.isEmpty(mediacodeStrSet)) {
            return false;
        }
        if (!mediacodeStrSet.contains(mediacode)) {
            if (log.isDebugEnabled()) {
                log.debug("activity: mediacode[{}] is not exists in mediacodeStrSet user conf. the mediacodeStrSet is {}",
                        mediacode, mediacodeStrSet);
            }
            return false;
        }
        return true;
    }

    /**
     * 设置key当日过期
     *
     * @param key key
     */
    private boolean setExpiredTime(String key) {
        try {
            Long expiredTime = redisCommonDAO.getExpiredTime(key);
            if (expiredTime > 0)//该键存在失效时间
            {
                return true;
            }
            long leaveTime = ChronoUnit.MILLIS.between(LocalDateTime.now(),LocalDateTime.of(LocalDate.now(),
                    LocalTime.MAX));
            if (expiredTime == -1)//不失效
            {
                redisCommonDAO.expire(key, leaveTime, TimeUnit.MILLISECONDS);
            } else if (expiredTime == -2)//不存在
            {
                redisCommonDAO.set(key, "0", leaveTime, TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(String.format("fail to expire key[%s]", key), e);
            return false;
        }
    }

    /**
     * 计算积分
     *
     * @param upMap 存储所得积分的map
     * @param upkey redisKey
     * @param point 所得积分
     * @param limit 最大限制
     */
    private void calculatePoint(Map<String, Long> upMap, String upkey, long point, long limit) {
        long up = upMap.getOrDefault(upkey, 0L);
        if (limit <= 0) {
            upMap.put(upkey, up + point);
        } else{
            upMap.put(upkey, Math.min(up + point, limit));
        }
    }

    /**
     * 计算影片观看次数
     *
     * @param mvMap  存储影片观看次数
     * @param mvkey  redis中的key
     * @param recond 记录
     */
    private void calculateViewCount(Map<String, Long> mvMap, String mvkey, Long recond) {
        Long mv = mvMap.getOrDefault(mvkey, 0L);
        mvMap.put(mvkey, recond + mv);
    }

    /**
     * 存入redis并记录日志
     *
     * @param logger log
     * @param upMap  用户积分
     * @param mvMap  媒资观看次数
     */
    private void store2RedisAndlog(Logger logger, Map<String, Long> upMap, Map<String, Long> mvMap) {
        if (upMap.isEmpty()) {
            return;
        }
        //存入redis
        redisCommonDAO.multiIncr(upMap);
        redisCommonDAO.multiIncr(mvMap);
        logger.info("{} keys for item {} update....", upMap.size(), ItemBean.WORLD_MATCH);

        String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        //String now = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        //日志存入redis
        upMap.forEach((key, second) ->
        {
            String userid = key.split(":")[1];
            logger.info("{},{},{}", now, userid, second);
        });

    }

    private void store2RedisAndlog(String actiId, Map<String, Long> upMap, Map<String, Long> mvMap) {
        if (upMap.isEmpty()) {
            return;
        }
        //存入redis
        try {
            Map<String, Long> newMap = redisCommonDAO.multiIncr(upMap);
            redisCommonDAO.multiIncr(mvMap);
            //logger.info("{} keys for item {} update....", upMap.size(), ItemBean.WORLD_MATCH);
            String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

            //String now = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
            //日志存入redis
            List logList = new ArrayList();
            StringBuffer stringBuffer = new StringBuffer();
            newMap.forEach((key, second) ->
            {
                //activityId-time-userid-积分
                String userid = key.split("_")[2];
                stringBuffer.append(actiId).append(Constant.LOG_SEPERATE).
                        append(now).append(Constant.LOG_SEPERATE).append(userid).
                        append(Constant.LOG_SEPERATE).append(second);
                logList.add(stringBuffer.toString());
                stringBuffer.delete(0, stringBuffer.length());
                //logger.info("{},{},{}", now, userid, second);
            });
            redisCommonDAO.multiLpush(Constant.LOG_KEY, logList);
            logList.clear();
        } catch (Exception e) {
            log.error("fail to store mv up to redis", e);
        }
    }
}
