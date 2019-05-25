package com.utstar.integral.schedule;

import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.ItemBean;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.redis.dao.RedisLockDAO;
import com.utstar.integral.service.ActivityService;
import com.utstar.integral.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 *
 * @author UTSC0928
 * @date 2018/6/5
 */
@Component
public class ScheduleJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJob.class);

    @Resource
    private RedisCommonDAO redisCommonDAO;

    @Resource
    private RedisLockDAO redisLockDAO;

    @Resource
    private CommonService commonService;

    @Resource
    private ActivityService activityService;

    //@Scheduled(cron = "${world.match.cron}")
    public void clearWorldMatchUserSecond(){
        //每天24点时候清0、涉及到001跟004的编排清0
        boolean getLock = redisLockDAO.tryGetDistributedLock(RedisKeyConstant.WORLD_MATCH_LOCK, RedisKeyConstant.HOSTNAME, RedisKeyConstant.WORLD_MATCH_LOCK_EXPIRE_TIME);
        if(getLock){
            LOGGER.info(RedisKeyConstant.HOSTNAME+" get lock "+RedisKeyConstant.WORLD_MATCH_LOCK);

            String worldMatchRegexKey = RedisKeyConstant.getUserItemKey("*", ItemBean.WORLD_MATCH);
            Set keys = redisCommonDAO.keys(worldMatchRegexKey);
            if(keys.size() > 0){
                redisCommonDAO.del(keys);
                LOGGER.info("clear {} key {}",worldMatchRegexKey,keys.size());
            }
            //不需要释放锁，锁设置了5分钟的过期时间
//            boolean releaseLock = redisLockDAO.releaseDistributedLock(RedisKeyConstant.WORLD_MATCH_LOCK, RedisKeyConstant.HOSTNAME);
//            LOGGER.info("{} release lock {} ",RedisKeyConstant.HOSTNAME,RedisKeyConstant.WORLD_MATCH_LOCK,releaseLock?"success":"failed");
        }
    }

    //@Scheduled(cron = "${summer.cron}")
    public void clearSummerUserSecond(){

        boolean getLock = redisLockDAO.tryGetDistributedLock(RedisKeyConstant.SUMMER_LOCK, RedisKeyConstant.HOSTNAME, RedisKeyConstant.SUMMER_LOCK_EXPIRE_TIME);
        if(getLock){
            LOGGER.info(RedisKeyConstant.HOSTNAME+" get lock "+RedisKeyConstant.SUMMER_LOCK);

            String summerRegexKey = RedisKeyConstant.getUserItemSubitemKey("*", ItemBean.SUMMER, "*");
            Set keys = redisCommonDAO.keys(summerRegexKey);
            if(keys.size() > 0){
                redisCommonDAO.del(keys);
                LOGGER.info("clear {} key {}",summerRegexKey,keys.size());
            }
        }
    }

    //@Scheduled(cron = "${voting.cron}")
    public void clearVotingUserSecond(){

        boolean getLock = redisLockDAO.tryGetDistributedLock(RedisKeyConstant.VOTING_LOCK, RedisKeyConstant.HOSTNAME, RedisKeyConstant.VOTING_LOCK_EXPIRE_TIME);
        if(getLock){
            LOGGER.info(RedisKeyConstant.HOSTNAME+" get lock "+RedisKeyConstant.VOTING_LOCK);
            String voting = RedisKeyConstant.getVotingItemKey("*",ItemBean.VOTING ,1 );
            Set keys = redisCommonDAO.keys(voting);
            if(keys.size() > 0){
                redisCommonDAO.del(keys);
                LOGGER.info("clear {} key {}",voting,keys.size());
            }
        }
    }

    //@Scheduled(cron = "${world.tag.sync.cron}")
    public void syncTagMediacode(){
        boolean getLock = redisLockDAO.tryGetDistributedLock(RedisKeyConstant.WORLD_MATCH_SYNC_TAG_LOCK, RedisKeyConstant.HOSTNAME, RedisKeyConstant.WORLD_MATCH_LOCK_EXPIRE_TIME);
        if(getLock){
            LOGGER.info("{} get lock {}",RedisKeyConstant.HOSTNAME,RedisKeyConstant.WORLD_MATCH_SYNC_TAG_LOCK);
            LOGGER.info("start reload mediacode from database to redis.....");

            /**
             * 10分钟从数据库重新同步对应code到redis，涉及到001跟004编排code同步
             */
            ItemBean worldBean = ItemBean.itemBeanMap.get(ItemBean.WORLD_MATCH);
            ItemBean CategoryBean = ItemBean.itemBeanMap.get(ItemBean.VOTING);
            commonService.loadWorldMatchMediacodeToRedis(worldBean.getSysids(),worldBean.getOptags());
            commonService.loadCategoryacodeToRedis(CategoryBean.getSysids(),CategoryBean.getMediaCodeCategory());
            LOGGER.info("end reload.....");
        }
    }

    //定时按规则同步mediacode到redis
    @Scheduled(cron = "${world.tag.sync.cron}")
    public void syncMediacode2Redis() {
        LOGGER.info("activity-schedule: sync mediacode to redis start.");

        commonService.syncMediacode2Redis();


        LOGGER.info("activity-schedule: sync mediacode to redis end.");
    }
}
