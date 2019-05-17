package com.utstar.integral.service.impl;

import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.UserViewLog;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.service.ActivityService;
import com.utstar.integral.service.CalculateService;
import com.utstar.integral.type.ActivityPointDelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * created by UTSC1244 at 2019/5/17 0017
 */
@Service
@Slf4j
public class CalculateServiceImpl implements CalculateService
{
    @Resource
    private RedisCommonDAO redisCommonDAO;
    @Resource
    private ActivityService activityService;

    @Override
    public boolean handleUserViewLogs(List<UserViewLog> userViewLogs)
    {
        //获取所有没有失效的活动
        List<ActivityEntity> activityEntities = activityService.findValidActivities();
        if(CollectionUtils.isEmpty(activityEntities))
        {
            log.info("activity:the activity query from db is empty.");
            return false;
        }
        
        for(ActivityEntity activityEntity : activityEntities)
            {
                //从redis中查找对应活动的mediacode
                String mediaType = activityEntity.getMediaType();
                if(StringUtils.isBlank(mediaType) ||
                        ((!Constant.MEDIA_TYPE_M.equals(mediaType))
                            &&(!Constant.MEDIA_TYPE_P.equals(mediaType))))

                {
                    mediaType = Constant.MEDIA_TYPE_P;
                }

                String activityId = activityEntity.getActivityId();
                Integer duration = activityEntity.getDuration();
                BigDecimal point = activityEntity.getPoint();
                ActivityPointDelType zeroDel = activityEntity.getZeroDel();

                //key:activity_${mediatype}_${activityid}_${duration}_${point}_${isZeroDel}_${limit}
                String key = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT, mediaType, activityId.toString(),
                        duration.toString(), point.toString(), zeroDel.toString());
                log.info("activity: the redis key is {}, activity is {}", key, activityEntity);

                Set<String> mediacodeStrSet = redisCommonDAO.hgetAllKey(key);
                if(CollectionUtils.isEmpty(mediacodeStrSet))
                {
                    log.error("activity: mediacode the user config is empty. the activity is {}", activityEntity);
                    return false;
                }

                for(UserViewLog userViewLog : userViewLogs)
                {

                }

            }
        );





        //从redis中取出所有需要采集的mediacode

        Set<String> mediacodeSetVoting = redisCommonDAO.hgetAllKey(Constant.MEDIA_NEED_TO_CALCULATE_FORMAT);
        return false;
    }
}
