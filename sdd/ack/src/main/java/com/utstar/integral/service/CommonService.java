package com.utstar.integral.service;

import com.alibaba.fastjson.JSONObject;
import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.log.LoggerBuilder;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.repository.btoc2.CommonRepository;
import com.utstar.integral.type.ActivityDataType;
import com.utstar.integral.type.ActivityStatusType;
import com.utstar.integral.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author UTSC0928
 * @date 2018/5/31
 */
@Component
public class CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
    private static final int HANDLE_LOG_SIZE = 1000;
    @Resource
    private CommonRepository commonRepository;
    @Resource
    private ActivityService activityService;
    @Resource
    private RedisCommonDAO redisCommonDAO;


    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    /**
     * 将对应栏目下面的Categoryacode存入redis的set里面
     */
    public void loadCategoryacodeToRedis(List<String> sysids, List<String> categoryacode) {
        long start = System.currentTimeMillis();
        List<String> mediacodeList = commonRepository.getMediacodeFromCategorya(sysids, categoryacode);

        if (mediacodeList.size() > 0) {

            //将categoryamediacode的数据重构
            stringRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {

                    StringRedisTemplate template = (StringRedisTemplate) operations;
                    //开启了事务
                    template.multi();
                    template.delete(RedisKeyConstant.getVOTINGItemKey(sysids.get(0)));
                    template.opsForSet().add(RedisKeyConstant.getVOTINGItemKey(sysids.get(0)), mediacodeList.toArray(new String[mediacodeList.size()]));
                    //执行事务
                    template.exec();
                    return null;
                }
            });

            long end = System.currentTimeMillis();
            LOGGER.info("load Categoryacode from db to redis key {} spend {} ms", RedisKeyConstant.getVOTINGItemKey(sysids.get(0)), (end - start));
        }
    }

    /**
     * 将移动世界杯专区对应的所有mediacode存入redis的set里面
     */
    public void loadWorldMatchMediacodeToRedis(List<String> sysids, List<String> optags) {
        long start = System.currentTimeMillis();
        List<String> mediacodeList = commonRepository.getMediacodeFromOptags(sysids, optags);

        if (mediacodeList.size() > 0) {

            //将世界杯的mediacode的数据重构
            stringRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {

                    StringRedisTemplate template = (StringRedisTemplate) operations;

                    //开启了事务
                    template.multi();

                    template.delete(RedisKeyConstant.INTEGRAL_MEDIACODE_WORLD);
                    template.opsForSet().add(RedisKeyConstant.INTEGRAL_MEDIACODE_WORLD, mediacodeList.toArray(new String[mediacodeList.size()]));

                    //执行事务
                    template.exec();

                    return null;
                }
            });

            long end = System.currentTimeMillis();
            LOGGER.info("load mediacode from db to redis key {} spend {} ms", RedisKeyConstant.INTEGRAL_MEDIACODE_WORLD, (end - start));
        }
    }

    public void syncMediacode2Redis() {
        String codeKey = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT,
                                    "*", "*", "*", "*", "*", "*");
        Set<String> keys = redisCommonDAO.keys(codeKey);
        if(CollectionUtils.isEmpty(keys)){
            return;
        }
        Set<String> set = new HashSet<>();

        for(String key: keys){
            String[] s = key.split("_");
            if(s.length == 7){
                String activityId = s[1];
                if(StringUtils.isNotBlank(activityId)){
                    ActivityEntity activityEntity = activityService.selectByActivityId(activityId);
                    if(activityEntity != null && activityEntity.getStatusType().equals(ActivityStatusType.VALID)
                            && activityEntity.getExpireTime().getTime() > System.currentTimeMillis()
                            && (!activityEntity.getDataType().equals(ActivityDataType.TYPE) || !activityEntity.getMediaCode().equals("2"))){
                        if(set.contains(activityId)) continue;
                        if(sychCode(activityEntity, true)){
                            set.add(activityId);
                        }
                        continue;
                    }
                }
            }
            redisCommonDAO.del(key);
        }
    }

    private boolean sychCode(ActivityEntity activityEntity, boolean isSync) {
        try {
            activityService.saveOrSyncMediaCode2Redis(activityEntity, true);
            return true;
        } catch (Exception e) {
            LOGGER.error("fail to sychcode.", e);
            return false;
        }
    }

    public void syncActiTotalCode() {
        List<ActivityEntity> activityEntities = activityService.findValidActivities();
        if(CollectionUtils.isEmpty(activityEntities)){
            return;
        }
        List<Map> medias = new ArrayList<>();
        for(ActivityEntity activityEntity: activityEntities) {
            String viewKey = String.format(Constant.MEDIA_VIEW_COUNT_FORMAT, activityEntity.getActivityId());
            activityService.sycnActiTotalCode(activityEntity.getActivityId(), medias, viewKey);
        }
    }

    public void handleLog(Long len) {
       if(len > HANDLE_LOG_SIZE){
           int count = len.intValue()/HANDLE_LOG_SIZE + 1;
           int l = HANDLE_LOG_SIZE;
           for(int i = 0; i <= count ; i++){
               if(i == count){
                   l = len.intValue() % HANDLE_LOG_SIZE;
               }
               List<String> list = redisCommonDAO.multiGetForList(Constant.LOG_KEY, HANDLE_LOG_SIZE);
               handleLog(list);
           }
       }else{
           List<String> list = redisCommonDAO.multiGetForList(Constant.LOG_KEY, len.intValue());
           handleLog(list);
       }

    }

    private void handleLog(List<String> list){
        if(CollectionUtils.isEmpty(list)) return;
        list.forEach(log ->{
            if(StringUtils.isNotBlank(log)){
                String[] logArr = log.split(Constant.LOG_SEPERATE);
                if(logArr != null && logArr.length == 5){
                    LoggerBuilder.getLogger(logArr[0]).info("{},{},{},{}",logArr[1],logArr[2],logArr[3],logArr[4]);
                }
            }
        });
        list.clear();
    }

    public void checkValidActi() {
        List<ActivityEntity> validActivities = activityService.findValidActivities();
        if(CollectionUtils.isEmpty(validActivities)){
            return;
        }
        validActivities.forEach(activityEntity -> {
            if(activityEntity.getExpireTime().getTime() > System.currentTimeMillis()){
                if(!activityEntity.getDataType().equals(ActivityDataType.TEXT))
                    if(!activityEntity.getDataType().equals(ActivityDataType.TYPE)
                        || !activityEntity.getMediaCode().equals("2")){
                        String codeKey = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT,
                                activityEntity.getActivityId(), "*", "*", "*", "*", "*");
                        Set<String> keys = redisCommonDAO.keys(codeKey);
                        if(CollectionUtils.isEmpty(keys)){
                            sychCode(activityEntity, true);
                        }
                    }
            }
        });
    }
}