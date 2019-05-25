package com.utstar.integral.service;

import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.repository.btoc2.CommonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author UTSC0928
 * @date 2018/5/31
 */
@Component
public class CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

    @Resource
    private CommonRepository commonRepository;
    @Resource
    private ActivityService activityService;


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

        String prefi;
        ActivityEntity activityEntity = null;
        activityService.saveOrSyncMediaCode2Redis(activityEntity,true);
    }
}