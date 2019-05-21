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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Override
    public boolean handleUserViewLogs(List<UserViewLog> userViewLogs)
    {
        if(CollectionUtils.isEmpty(userViewLogs))
        {
            log.info("activity: handle viewlogs is empty. handle is over");
            return false;
        }
        //获取所有没有失效的活动
        log.info("activity: handle viewlogs start at {}, viewLogs size is {}",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),
                userViewLogs.size());
        List<ActivityEntity> activityEntities = activityService.findValidActivities();
        if (CollectionUtils.isEmpty(activityEntities))
        {
            log.info("activity: query valid activity from db is empty.");
            return false;
        }
        for (ActivityEntity activityEntity : activityEntities)
        {
            //存放计算的用户积分map key:redis中的key
            Map<String, Long> upMap = new HashMap<>();
            //存放计算的用户观看次数map key:redis中的key
            Map<String, Long> mvMap = new HashMap<>();
            //从redis中查找对应活动的mediacode
            // TODO 暂时没有mediaType 此处先给定一个默认值: p
            String mediaType = activityEntity.getMediaType();
            if (StringUtils.isBlank(mediaType) ||
                    ((!Constant.MEDIA_TYPE_M.equals(mediaType))
                            && (!Constant.MEDIA_TYPE_P.equals(mediaType)))) {
                mediaType = Constant.MEDIA_TYPE_P;
            }
            String activityId = activityEntity.getActivityId();
            Integer duration = activityEntity.getDuration();
            long point = activityEntity.getPoint();
            long limit = activityEntity.getLimit();
            ActivityPointDelType zeroDel = activityEntity.getZeroDel();
            /**
             * 存放用户配置用于需要统计积分的媒资code
             * key:activity_${mediatype}_${activityid}_${duration}_${point}_${isZeroDel}_${limit}
             */
            String key = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT, mediaType, activityId,
                    duration.toString(), String.valueOf(point), zeroDel.toString());
            log.info("activity: the redis key is {}, activity is {}", key, activityEntity);

            //取出配置的媒资code
            Set<String> mediacodeStrSet = redisCommonDAO.hgetAllKey(key);
            if (CollectionUtils.isEmpty(mediacodeStrSet))
            {
                log.error("activity: mediacode the user config is empty. the activity is {}", activityEntity);
                return false;
            }

            for (UserViewLog userViewLog : userViewLogs)
            {
                String mediacode = userViewLog.getMediacode();

                if (!mediacodeStrSet.contains(mediacode))
                {//不在用户配置内
                    if(log.isDebugEnabled())
                    {
                        log.debug("activity: mediacode[{}] is not exists in mediacodeStrSet user conf. the mediacodeStrSet is {}",
                                  mediacode, mediacodeStrSet);
                    }
                    continue;
                }
                long userViewTimeMills = userViewLog.getViewEnd().getTime() - userViewLog.getViewStart().getTime();
                if (userViewTimeMills < duration * 1000)
                {//观看时长小于用户设置的最小时长
                    if(log.isDebugEnabled())
                    {
                        log.debug("activity: the viewTimeMills[{}] of media is less than durationMills[{}] user set",
                                userViewTimeMills, duration * 1000);
                    }
                    continue;
                }
                ActivityPointDelType isZeroDel = activityEntity.getZeroDel();

                //用户积分key up_${activityid}_${userid}
                String upkey = String.format(Constant.USER_POINT_FORMAT, activityEntity.getActivityId(), userViewLog.getUserid());
                //媒资观看次数 mv_${activityid}_${mediacode}
                String mvkey = String.format(Constant.MOVIE_VIEW_FORMAT, activityEntity.getActivityId(), userViewLog.getMediacode());
                switch (isZeroDel)
                {
                    case DEL_DAY://每日清零
                        //设置失效时间
                        setExpiredTime(new String[]{upkey, mvkey});
                        break;
                    case NOT_DEL://持续累积（不清零）
                        break;
                }
                //计算积分
                calculatePoint(upMap, upkey, point, limit);
                //计算观看次数
                calculateViewCount(mvMap, mvkey, userViewLog.getSecond());


            }
            //1个活动计算结束，将计算结果存入redis
            //LoggerFactory.getLogger(activityEntity.getActivityId()) TODO basePath从activityEntity中取
            Logger logger = LoggerBuilder.getLogger(activityEntity.getActivityId(), "");
            store2RedisAndlog(logger, upMap, mvMap);
            return true;

        }

        return false;
    }

    /**
     * 设置key当日过期
     * @param keys 多个key
     */
    private void setExpiredTime(String... keys)
    {
        for(String key : keys)
        {
            setExpiredTime(key);
        }

    }

    /**
     * 设置key当日过期
     * @param key key
     */
    private void setExpiredTime(String key)
    {
        Long expiredTime = redisCommonDAO.getExpiredTime(key);
        if(expiredTime > 0)//该键存在失效时间
        {
            return;
        }
        long leaveTime = ChronoUnit.MILLIS.between(LocalDateTime.of(LocalDate.now(), LocalTime.MAX), Instant.now());
        if(expiredTime == -1)//不失效
        {
            redisCommonDAO.expire(key, leaveTime, TimeUnit.MILLISECONDS);
        }else if(expiredTime == -2)//不存在
        {
            redisCommonDAO.set(key, "0", leaveTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 计算积分
     * @param upMap 存储所得积分的map
     * @param upkey redisKey
     * @param point 所得积分
     * @param limit 最大限制
     */
    private void calculatePoint(Map<String, Long> upMap, String upkey, long point, long limit)
    {
        long up = upMap.getOrDefault(upkey, 0L);
        if(limit <= 0|| up > limit)
        {
            upMap.put(upkey, Math.min(up + point, limit));
        }else if(up < limit)
        {
            upMap.put(upkey, limit);
        }
    }

    /**
     * 计算影片观看次数
     * @param mvMap 存储影片观看次数
     * @param mvkey redis中的key
     * @param recond 记录
     */
    private void calculateViewCount(Map<String, Long> mvMap, String mvkey, Long recond)
    {
        Long mv = mvMap.getOrDefault(mvkey, 0L);
        mvMap.put(mvkey, recond + mv);
    }

    /**
     * 存入redis并记录日志
     * @param logger log
     * @param upMap 用户积分
     * @param mvMap 媒资观看次数
     */
    private void store2RedisAndlog(Logger logger, Map<String, Long> upMap, Map<String, Long> mvMap)
    {
        if(upMap.isEmpty())
        {
            return;
        }
        //存入redis
        redisCommonDAO.multiIncr(upMap);
        redisCommonDAO.multiIncr(mvMap);
        logger.info("{} keys for item {} update....",upMap.size(),ItemBean.WORLD_MATCH);

        String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        //String now = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        //存入日志
        upMap.forEach((key,second) ->
        {
            String userid = key.split(":")[1];
            logger.info("{},{},{}",now,userid,second);
        });

    }
}
