package com.utstar.integral.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utstar.integral.Exception.CoreException;
import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.TableEntity;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.repository.btoc2.ActivityRepository;
import com.utstar.integral.repository.credb.MediaCodeRepository;
import com.utstar.integral.service.ActivityService;
import com.utstar.integral.type.ActivityDataType;
import com.utstar.integral.type.ActivityPointDelType;
import com.utstar.integral.type.ActivityStatusType;
import com.utstar.integral.utils.Assert;
import com.utstar.integral.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private RedisCommonDAO redisCommonDAO;
    @Resource
    private ActivityRepository activityRepository;
    @Resource
    private Environment environment;
    @Resource
    private MediaCodeRepository mediaCodeRepository;

    private Lock lock = new ReentrantLock();

    @Override
    public TableEntity list(ActivityEntity param, int start, int limit) {
        TableEntity<ActivityEntity> tableEntity = new TableEntity();
        Page<ActivityEntity> pageActivity = getPageActivity(param, start / limit, limit);

        tableEntity.setTotal(pageActivity.getTotalElements());
        tableEntity.setRows(pageActivity.getContent());
        return tableEntity;
    }

    @Override
    public void add(ActivityEntity activityEntity, String username) {
        validateActivityEntity(activityEntity);
        try {
            if (activityEntity.getDataType().equals(ActivityDataType.TEXT))
            {
                String mediacodeStr = redisCommonDAO.get(String.format(Constant.IMPORT_EXCEL_FORMAT,
                        activityEntity.getUuid()));
                Assert.isNotEmpty(mediacodeStr, "请上传文件");

                List<String> codeList = JsonUtils.jsonToList(mediacodeStr, String.class);
                Assert.isNotEmpty(codeList, "请重新上传文件");
            }
            activityEntity.setClusterId(environment.getProperty("clusterId"));
            Date date = new Date();
            if (activityEntity.getId() == null)
            {
                activityEntity.setId(redisCommonDAO.incr(Constant.ACTIVITY_PRIMARY_KEY));
                activityEntity.setCreateDate(date);
            }
            activityEntity.setLastUpdate(date);
            activityEntity.setExpireTime(date);
            activityRepository.save(activityEntity);

            saveOrSyncMediaCode2Redis(activityEntity, false);

        } catch (CoreException e) {
            throw e;
        } catch (Exception e) {
            log.error("activity:add fail to add activity", e);
            throw new CoreException(Constant.FAIL);
        }
        //storeToRedis(activityEntity, getActivityKeyByUsername(username));
    }

    @Override
    public void saveOrSyncMediaCode2Redis(ActivityEntity activityEntity, boolean isSync)
    {
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
        String sysIdStr = activityEntity.getSysId();
        String[] sysIdArr = sysIdStr.split(",");

        ActivityDataType dataType = activityEntity.getDataType();
        Map<String, Set<String>> codeMap = null;
        switch (dataType)
        {
            case TEXT:
                if(isSync){
                    return;
                }
                codeMap = handleTextMediaCode(activityEntity);
                break;
            case TYPE:
                codeMap = handleTypeMediaCode(activityEntity);
                break;
            case OPTAG:
                codeMap = handleOptagMediaCode(activityEntity);
                break;
            case COLUMN:
                codeMap = handleColumnMediaCode(activityEntity);
                break;
            case BASETAG:
                codeMap = handleBasetagMediaCode(activityEntity);
                break;
            case CATEGORY:
                codeMap = handleCategoryMediaCode(activityEntity);
                break;
        }
        Assert.isFalse(CollectionUtils.isEmpty(codeMap), "没找到对应的媒资");
        for(String sysid : sysIdArr)
        {
            /**
             * 存放用户配置用于需要统计积分的媒资code
             * key:activity_${mediatype}_${activityid}_${duration}_${point}_${isZeroDel}_${limit}_${sysid}
             */
            String key = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT, mediaType, activityId,
                    duration.toString(), String.valueOf(point), zeroDel.toString(), sysid);
            Set<String> set = codeMap.get(sysid);
            if(!CollectionUtils.isEmpty(set)) redisCommonDAO.multiPipeHset(key, set);
        }
    }

    @Override
    public ActivityEntity selectByActivityId(String activityId) {
        return activityRepository.findByActivityId(activityId);
    }

    private Map<String, Set<String>> handleCategoryMediaCode(ActivityEntity activityEntity)
    {
        Map<String, Set<String>> mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByCategoryIdsAndSysids(
                CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleBasetagMediaCode(ActivityEntity activityEntity)
    {
        Map<String, Set<String>> mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByBasetag(
                CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleColumnMediaCode(ActivityEntity activityEntity)
    {
        Map<String, Set<String>> mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByColumnCodes(
                CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleOptagMediaCode(ActivityEntity activityEntity)
    {
        Map<String, Set<String>> mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByoptags(
                CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleTypeMediaCode(ActivityEntity activityEntity)
    {
            Map<String, Set<String>> mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByChargetypes(
                    CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                    CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleTextMediaCode(ActivityEntity activityEntity)
    {
        Map<String, Set<String>> codeMap = new HashMap<>();

        String mediacodeStr = redisCommonDAO.get(String.format(Constant.IMPORT_EXCEL_FORMAT,
                activityEntity.getUuid()));
        Assert.isNotEmpty(mediacodeStr, "请上传文件");

        Set<String> codeSet = JsonUtils.jsonToSet(mediacodeStr, String.class);
        Assert.isNotEmpty(codeSet, "请重新上传文件");

        for(String sysid : activityEntity.getSysId().split(","))
        {
            codeMap.put(sysid, codeSet);
        }

        return codeMap;
    }
    @Override
    public void edit(ActivityEntity activityEntity, String username) {
        validateActivityEntity(activityEntity);

        String activityKey = getActivityKeyByUsername(username);
        try {
            List<String> sourceList = redisCommonDAO.lrange(activityKey, 0, -1);
            if (!sourceList.isEmpty()) {
                redisCommonDAO.del(activityKey);
                //String id = activityEntity.getId();
                for (int i = sourceList.size() - 1; i >= 0; i--) {
                    String activityStr = sourceList.get(i);
                    JSONObject jsonObject = JSONObject.parseObject(activityStr);
                    String sourceId = jsonObject.getString("id");
                    /*if(sourceId.equals(id))
                    {
                        redisCommonDAO.lpush(activityKey,JSON.toJSONString(activityEntity));
                    }else
                    {
                        redisCommonDAO.lpush(activityKey,activityStr);
                    }*/
                }
            }

        } catch (Exception e) {
            log.error("activity-add:Fail to edit activity.", e);
            throw new CoreException("操作失败");
        }
    }


    @Override
    public void del(ActivityEntity activityEntity, String username) {
        //Assert.isNotEmpty(activityEntity.getId(), "请选择一条记录");

        String activityKey = getActivityKeyByUsername(username);

        try {
            List<String> sourceList = redisCommonDAO.lrange(activityKey, 0, -1);
            if (!sourceList.isEmpty()) {
                redisCommonDAO.del(activityKey);
                //String id = activityEntity.getId();
                for (int i = sourceList.size() - 1; i >= 0; i--) {
                    String activityStr = sourceList.get(i);
                    JSONObject jsonObject = JSONObject.parseObject(activityStr);
                    String sourceId = jsonObject.getString("id");
                    /*if(!sourceId.equals(id))
                    {
                        redisCommonDAO.lpush(activityKey,activityStr);
                    }*/
                }
            }
        } catch (Exception e) {
            log.error("activity-add:Fail to add activity.", e);
            throw new CoreException("操作失败");
        }
    }

    @Override
    public void storeMediacode2Redis(List<List<String>> datas, String uuid) {
        Assert.isNotEmpty(uuid, Constant.FAIL);
        Assert.isNotEmpty(datas, "导入的数据为空!");
        List<String> list = new ArrayList<>();
        datas.forEach(d ->
        {
            if (StringUtils.isNotBlank(d.get(0))) {
                list.add(d.get(0));
            }

        });
        Assert.isNotEmpty(list, "导入的数据不合法!");
        redisCommonDAO.set(String.format(Constant.IMPORT_EXCEL_FORMAT, uuid), JsonUtils.objectToJson(list));
    }

    @Override
    public List<ActivityEntity> findValidActivities()
    {
        List<ActivityEntity> activityEntities = null;
        try
        {
            Date now = new Date();
            Specification<ActivityEntity> specification = (root, criteriaQuery, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder
                        .greaterThan(root.get("expireTime").as(Date.class), now));
                predicates.add(criteriaBuilder
                        .lessThan(root.get("expireTime").as(Date.class), now));
                predicates.add(criteriaBuilder
                        .equal(root.get("statusType").as(Enum.class), ActivityStatusType.VALID));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            activityEntities = activityRepository.findAll(specification);
        } catch (Exception e) {
            log.error("activity: fail to get valid activities.", e);
        }
        return activityEntities;
    }

    private String getActivityKeyByUsername(String username) {
        return String.format(Constant.ACTIVITY_KEY_FORMAT, username);
    }

    /**
     * 校验活动实体类
     *
     * @param activityEntity 实体类
     */
    private void validateActivityEntity(ActivityEntity activityEntity) {
        Assert.isNotEmpty(activityEntity.getActivityId(), "请填写活动id");
        Assert.isNotEmpty(activityEntity.getActivityName(), "请填写活动名称");
        Assert.isNotNull(activityEntity.getValidTime(), "请填写开始时间");
        Assert.isNotNull(activityEntity.getExpireTime(), "请填写结束时间");
        Assert.isTrue(activityEntity.getExpireTime().getTime() >
                activityEntity.getValidTime().getTime(), "结束时间应大于开始");
        Assert.isNotEmpty(activityEntity.getSysId(), "请选择运营商");
        Assert.isNotNull(activityEntity.getDuration(), "请填写最小观看时长");
        Assert.isTrue(activityEntity.getDuration() > 0, "最小观看时长应大于0");
        Assert.isNotNull(activityEntity.getPoint(), "请填写积分");
        Assert.isTrue(activityEntity.getPoint() > 0, "积分应大于0");
        Assert.isNotEmpty(activityEntity.getMediaCode(), "请选择数据来源");
    }

    /**
     * @param activityEntity 活动实体
     * @param activityKey    活动在redis中存储的key
     */
    private synchronized void storeToRedis(ActivityEntity activityEntity, String activityKey) {
        try {
            List<String> sourceList = redisCommonDAO.lrange(activityKey, 0, -1);

            if (!sourceList.isEmpty()) {
                //String id = activityEntity.getId();
                for (String activityStr : sourceList) {
                    JSONObject jsonObject = JSONObject.parseObject(activityStr);
                    String sourceId = jsonObject.getString("id");

                    /*if(sourceId.equals(id))
                    {
                        log.error("activity-add:id:{} is repeated", id);
                        return;
                    }*/
                }
            }
            redisCommonDAO.lpush(activityKey, JSON.toJSONString(activityEntity));
        } catch (Exception e) {
            log.error("activity-add:Fail to add activity.", e);
            throw new CoreException("操作失败");
        }
    }

    public Page<ActivityEntity> getPageActivity(ActivityEntity activityEntity, int pageNumber, int pageSize) {
        Specification querySpeci = (root, criteriaQuery, criteriaBuilder) ->
        {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(activityEntity.getActivityId()))
            {
                predicates.add(criteriaBuilder
                        .like(root.get("activityId"), "%" + activityEntity.getActivityId() + "%"));
            }
            if (StringUtils.isNotBlank(activityEntity.getActivityName()))
            {
                predicates.add(criteriaBuilder
                        .like(root.get("activityName"), "%" + activityEntity.getActivityName() + "%"));
            }
            if (activityEntity.getStatusType() != null)
            {
                predicates.add(criteriaBuilder
                        .equal(root.get("statusType"), activityEntity.getStatusType().ordinal()));
            }
            if (null != activityEntity.getValidTime())
            {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("validTime").as(Date.class), activityEntity.getValidTime()));
            }
            if (null != activityEntity.getExpireTime()) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("expireTime").as(Date.class), activityEntity.getExpireTime()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize);
        return activityRepository.findAll(querySpeci, pageRequest);
    }
}
