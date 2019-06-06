package com.utstar.integral.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.Exception.CoreException;
import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.TableEntity;
import com.utstar.integral.log.LoggerBuilder;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.redis.dao.RedisLockDAO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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

    private Logger USER_LOG = LoggerFactory.getLogger("USEROPT");

    @Autowired
    private RedisLockDAO redisLockDAO;

    @Override
    public TableEntity list(ActivityEntity param, int start, int limit) {
        TableEntity<ActivityEntity> tableEntity = new TableEntity();
        Page<ActivityEntity> pageActivity = getPageActivity(param, start / limit, limit);
        List<ActivityEntity> result = Collections.EMPTY_LIST;
        if (!CollectionUtils.isEmpty(pageActivity.getContent())) {
            Date now = new Date();
            pageActivity.getContent()
                    .forEach(activityEntity -> {
                        activityEntity.setIsValid(
                                isValid(activityEntity));
                    });
            result = pageActivity.getContent().stream().sorted
                    ((a1, a2) -> {return a2.getActivityId().compareTo(a1.getActivityId());

                    })
                    .collect(Collectors.toList());
        }
        tableEntity.setTotal(pageActivity.getTotalElements());
        tableEntity.setRows(result);
        return tableEntity;
    }

    private boolean isValid(ActivityEntity activityEntity){
        Date now = new Date();
        return activityEntity.getStatusType().equals(ActivityStatusType.VALID)
                && activityEntity.getExpireTime().compareTo(now) > 0
                && activityEntity.getValidTime().compareTo(now) <= 0;
    }

    @Override
    public void add(ActivityEntity activityEntity, String username) throws CoreException {
        USER_LOG.info("opt-user:add start. username is {}, param is {}", username, activityEntity);

        boolean islock = false;
        String lockValue = UUID.randomUUID().toString();
        validateActivityEntity(activityEntity);
        try {
            if (activityEntity.getDataType().equals(ActivityDataType.TEXT)) {
                String mediacodeStr = redisCommonDAO.get(String.format(Constant.IMPORT_EXCEL_FORMAT,
                        activityEntity.getMediaCode()));
                Assert.isNotEmpty(mediacodeStr, "请上传文件");
                List<String> codeList = JsonUtils.jsonToList(mediacodeStr, String.class);
                Assert.isNotEmpty(codeList, "请重新上传文件");
            }
            activityEntity.setClusterId(environment.getProperty("clusterId", "1"));
            Date date = new Date();

            activityEntity.setOperation(username);
            activityEntity.setId(redisCommonDAO.incr(Constant.ACTIVITY_PRIMARY_KEY));
            activityEntity.setLastUpdate(date);
            activityEntity.setCreateDate(date);

            islock = redisLockDAO.tryGetDistributedLock(Constant.ACTI_ADD_LOCK, lockValue,3*60);
            log.info("activity-add-lock:{} lock {}.result is {}",lockValue, Constant.ACTI_ADD_LOCK, islock);

            Assert.isTrue(islock, "服务器忙,请稍后再试");
            validateActivityIsExists(activityEntity.getActivityId());

            saveOrSyncMediaCode2Redis(activityEntity, false);
            ActivityEntity save = activityRepository.save(activityEntity);

            USER_LOG.info("opt-user:add success.username is {}, param is {}. result is {}",
                    username, activityEntity, save);
        } catch (CoreException e) {
            String msg = String.format("opt-user:add fail. username is %s, param is %s.",
                    username, activityEntity.toString());
            USER_LOG.info(msg, e);
            throw e;
        } catch (Exception e) {
            String msg = String.format("opt-user:add fail. username is %s, param is %s.",
                    username, activityEntity.toString());
            USER_LOG.info(msg, e);
            log.error("activity:add fail to add activity", e);
            throw new CoreException(Constant.FAIL);
        }finally {
            if(islock){
                boolean releaseLock = redisLockDAO.releaseDistributedLock(Constant.ACTI_ADD_LOCK, lockValue);
                log.info("activity-add-lock:{} release lock {}. result is {}",lockValue,
                        Constant.ACTI_ADD_LOCK, releaseLock?"success":"failed");
            }

        }
        //storeToRedis(activityEntity, getActivityKeyByUsername(username));
    }

    private void validateActivityIsExists(String activityId) {
        Assert.isTrue(activityRepository.findByActivityId(activityId) == null, "活动id已存在");
    }

    @Override
    public void edit(ActivityEntity activityEntity, String username) {
        USER_LOG.info("opt-user:edit start. username is {}, param is {}", username, activityEntity);

        validateActivityEntity(activityEntity);
        ActivityEntity source = activityRepository.findByActivityId(activityEntity.getActivityId());
        Assert.isNotNull(source, "请选择一条记录");
        try {
            if (activityEntity.getDataType().equals(ActivityDataType.TEXT)
                    && !activityEntity.getMediaCode().equals(source.getMediaCode())) {
                String mediacodeStr = redisCommonDAO.get(String.format(Constant.IMPORT_EXCEL_FORMAT,
                        activityEntity.getMediaCode()));
                Assert.isNotEmpty(mediacodeStr, "请上传文件");
                List<String> codeList = JsonUtils.jsonToList(mediacodeStr, String.class);
                Assert.isNotEmpty(codeList, "请重新上传文件");
            }
            activityEntity.setClusterId(environment.getProperty("clusterId", "1"));
            Date date = new Date();
            //activityEntity.setId(redisCommonDAO.incr(Constant.ACTIVITY_PRIMARY_KEY));
            activityEntity.setLastUpdate(date);
            activityEntity.setOperation(username);

            if ((!source.getDataType().equals(activityEntity.getDataType()))
                    ||(!source.getMediaCode().equals(activityEntity.getMediaCode()))
                    ||(!source.getDuration().equals(activityEntity.getDuration()))
                    ||(source.getPoint() != activityEntity.getPoint())
                    ||(!source.getZeroDel().equals(activityEntity.getZeroDel()))
                    ||(!source.getSysId().equals(activityEntity.getSysId()))
                    ||(source.getLimit() != activityEntity.getLimit()
                    || needToGetCode(source) != needToGetCode(activityEntity))
            ) {
                //需要更新mediacode
                saveOrSyncMediaCode2Redis(activityEntity, false);
            }

            activityRepository.save(activityEntity);
            USER_LOG.info("opt-user:edit success. username is {}. edit before is {}, result is {}",
                    username, source, activityEntity);
        } catch (CoreException e) {
            String msg = String.format("opt-user:edit fail. username is %s, param is %s.",
                    username, activityEntity.toString());
            USER_LOG.info(msg, e);
            throw e;
        } catch (Exception e) {
            String msg = String.format("opt-user:edit fail. username is %s, param is %s.",
                    username, activityEntity.toString());
            log.error(msg, e);

            throw new CoreException(Constant.FAIL);
        }
    }

    @Override
    public void saveOrSyncMediaCode2Redis(ActivityEntity activityEntity, boolean isSync) {
        String activityId = activityEntity.getActivityId();
        Integer duration = activityEntity.getDuration();
        long point = activityEntity.getPoint();
        ActivityPointDelType zeroDel = activityEntity.getZeroDel();
        String sysIdStr = activityEntity.getSysId();
        String[] sysIdArr = sysIdStr.split(",");

        if(!isSync && !needToGetCode(activityEntity)){
            delMediacodeKey(activityId);
            return;
        }
        ActivityDataType dataType = activityEntity.getDataType();
        if(ActivityDataType.TYPE.equals(dataType) && activityEntity.getMediaCode().equals("2")){
            delMediacodeKey(activityId);
            return;
        }
        log.debug("dataType is {}", dataType);
        Map<String, Set<String>> codeMap = null;
        switch (dataType) {
            case TEXT:
                if (isSync) {
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
        boolean isDel = false;
        for (String sysid : sysIdArr) {
            /**
             * 存放用户配置用于需要统计积分的媒资code
             * key:activity_${activityid}_${duration}_${point}_${isZeroDel}_${limit}_${sysid}
             */
            String key = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT, activityId,
                    duration.toString(), point, zeroDel.ordinal(), String.valueOf(activityEntity.getLimit()), sysid);
            Set<String> set = codeMap.get(sysid);
            if (set == null) continue;
            if (isSync) {
                redisCommonDAO.syncMediacode(key, set);
            } else {
                if(!isDel){
                    delMediacodeKey(activityId);
                }
                isDel = true;
                Assert.isTrue(redisCommonDAO.multiAddSet(key, set), Constant.FAIL);
            }
        }
    }

    private void delMediacodeKey(String activityId)
    {
        String activityAllKey = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT, activityId,
                "*", "*", "*", "*", "*");
        Set keys = redisCommonDAO.keys(activityAllKey);
        if(!CollectionUtils.isEmpty(keys)){
            redisCommonDAO.del(keys);
        }
    }

    @Override
    public ActivityEntity selectByActivityId(String activityId) {
        return activityRepository.findByActivityId(activityId);
    }

    private Map<String, Set<String>> handleCategoryMediaCode(ActivityEntity activityEntity) {
        Map<String, Set<String>> mediaCodeByChargetypes = null;
        try {
            mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByCategoryIdsAndSysids(
                    CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                    CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
            return mediaCodeByChargetypes;
        } catch (Exception e) {
            log.error(String.format("fail to find basetag mediacode, the activity is %s", activityEntity.toString()), e);
        }
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleBasetagMediaCode(ActivityEntity activityEntity) {
        Map<String, Set<String>> mediaCodeByChargetypes = null;
        try {
            mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByBasetag(
                    CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                    CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
            return mediaCodeByChargetypes;
        } catch (Exception e) {
            log.error(String.format("fail to find basetag mediacode, the activity is %s", activityEntity.toString()), e);
        }
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleColumnMediaCode(ActivityEntity activityEntity) {
        Map<String, Set<String>> mediaCodeByChargetypes = null;
        try {
            mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByColumnCodes(
                    CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                    CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        } catch (Exception e) {
            log.error(String.format("fail to find column mediacode, the activity is %s", activityEntity.toString()), e);
        }
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleOptagMediaCode(ActivityEntity activityEntity) {
        Map<String, Set<String>> mediaCodeByChargetypes = null;
        try {
            mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByoptags(
                    CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                    CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        } catch (Exception e) {
            log.error(String.format("fail to find optag mediacode, the activity is %s", activityEntity.toString()), e);
        }
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleTypeMediaCode(ActivityEntity activityEntity) {
        Map<String, Set<String>> mediaCodeByChargetypes = null;
        try {
            mediaCodeByChargetypes = mediaCodeRepository.getMediaCodeByChargetypes(
                    CollectionUtils.arrayToList(activityEntity.getMediaCode().split(",")),
                    CollectionUtils.arrayToList(activityEntity.getSysId().split(",")));
        } catch (Exception e) {
            log.error(String.format("fail to find type mediacode, the activity is %s", activityEntity.toString()), e);
        }
        return mediaCodeByChargetypes;
    }

    private Map<String, Set<String>> handleTextMediaCode(ActivityEntity activityEntity) {
        Map<String, Set<String>> codeMap = new HashMap<>();

        try {
            String mediacodeStr = redisCommonDAO.get(String.format(Constant.IMPORT_EXCEL_FORMAT,
                    activityEntity.getMediaCode()));
            if(StringUtils.isBlank(mediacodeStr)){
                ActivityEntity sourceActivity = activityRepository.findByActivityId(activityEntity.getActivityId());
                if(sourceActivity != null){
                    for (String sysid : sourceActivity.getSysId().split(",")) {
                        String key = String.format(Constant.MEDIACODE_FROM_CONFIG_FORMAT
                                ,activityEntity.getActivityId(),
                                activityEntity.getDuration().toString(), String.valueOf(activityEntity.getPoint()),
                                activityEntity.getZeroDel().ordinal(), activityEntity.getLimit(),sysid);
                        log.info("activity: the redis key is {}, activity is {}", key, activityEntity);
                        Set<String> mediacodeStrSet = redisCommonDAO.sMembers(key);
                        if (CollectionUtils.isEmpty(mediacodeStrSet)) continue;
                        codeMap.put(sysid, mediacodeStrSet);
                    }
                    return codeMap;
                }
            }
            Assert.isNotEmpty(mediacodeStr, "请上传文件");

            Set<String> codeSet = JsonUtils.jsonToSet(mediacodeStr, String.class);
            Assert.isNotEmpty(codeSet, "请重新上传文件");

            for (String sysid : activityEntity.getSysId().split(",")) {
                codeMap.put(sysid, codeSet);
            }
        } catch (Exception e) {
            log.error(String.format("fail to find text mediacode, the activity is %s", activityEntity.toString()), e);
        }

        return codeMap;
    }

    @Override
    public void del(List<String> ids, String username) {
        USER_LOG.info("opt-user:del start. param is {}. username is {}",
                ids, username);
        Assert.isNotEmpty(ids, "请选择一条记录");
        //String activityKey = getActivityKeyByUsername(username);
        Date date = new Date();
        try {
            List<ActivityEntity> activityEntities = activityRepository.findByActivityIdIn(ids);
            Assert.isNotEmpty(ids, "请选择一条记录");
            activityEntities.forEach(activityEntity -> {
                activityEntity.setOperation(username);
                activityEntity.setLastUpdate(date);
                activityEntity.setStatusType(ActivityStatusType.DEL);
            });
            activityRepository.save(activityEntities);
            USER_LOG.info("opt-user:del success. param is {}. username is {}",
                    ids, username);
        } catch (Exception e) {
            String msg = String.format("opt-user:del fail. param is %s. username is %s", ids.toString(), username);
            USER_LOG.info("opt-user:del fail. param is {}. username is {}",
                    ids, username);
            log.error("activity-add:Fail to del activity.", e);
            throw new CoreException(Constant.FAIL);
        }
    }

    @Override
    public void storeMediacode2Redis(List<List<String>> datas, String uuid) {
        Assert.isNotEmpty(uuid, Constant.FAIL);
        Assert.isNotEmpty(datas, "导入的数据为空!");
        List<String> list = new ArrayList<>();
        datas.forEach(d -> {
            if (StringUtils.isNotBlank(d.get(0))&& StringUtils.isNumeric(d.get(0))) {
                list.add(d.get(0));
            }
        });
        Assert.isNotEmpty(list, "导入的数据不合法!");
        redisCommonDAO.set(String.format(Constant.IMPORT_EXCEL_FORMAT, uuid), JsonUtils.objectToJson(list), 5, TimeUnit.MINUTES);
    }

    @Override
    public List<ActivityEntity> findValidActivities() {
        List<ActivityEntity> activityEntities = null;
        try {
            Date now = new Date();
            Specification<ActivityEntity> specification = (root, criteriaQuery, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder
                        .greaterThan(root.get("expireTime").as(Date.class), now));
                predicates.add(criteriaBuilder
                        .lessThan(root.get("validTime").as(Date.class), now));
                predicates.add(criteriaBuilder
                        .equal(root.get("statusType"), ActivityStatusType.VALID.ordinal()));
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
        Assert.isTrue(activityEntity.getMediaCode().length() <= 64, "媒资内容所选过多");
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
            if (StringUtils.isNotBlank(activityEntity.getActivityId())) {
                predicates.add(criteriaBuilder
                        .like(root.get("activityId"), "%" + activityEntity.getActivityId() + "%"));
            }
            if (StringUtils.isNotBlank(activityEntity.getActivityName())) {
                predicates.add(criteriaBuilder
                        .like(root.get("activityName"), "%" + activityEntity.getActivityName() + "%"));
            }
            if (activityEntity.getStatusType() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("statusType"), activityEntity.getStatusType().ordinal()));
            } else {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("statusType"), ActivityStatusType.VALID.ordinal()),
                        criteriaBuilder.equal(root.get("statusType"), ActivityStatusType.EXPIRED.ordinal())));
            }
            if (null != activityEntity.getValidTime()) {
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

    @Override
    public void sycnActiTotalCode(String activityId, List<Map> medias, String viewKey) {
        String upkey = String.format(Constant.MOVIE_VIEW_FORMAT, activityId, "*", "*", "*");
        Set<String> keys = redisCommonDAO.keys(upkey);
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        Map<String, Set<String>> mediacodeMap = new HashMap<>();
        for (String key : keys) {
            //按媒资分组  mv_${activityid}_${mediacode}_${sysid}
            /*int first = key.indexOf("_", 3);
            int last = key.lastIndexOf("_");
            if (first == -1 || last == -1 || first <= last) {
                continue;
            }*/
            if(StringUtils.isBlank(key)) continue;
            String[] arr = key.split("_");
            if(arr.length != 4) continue;

            String mediacode = arr[2];
            Set<String> set = mediacodeMap.get(mediacode);
            if (set == null) {
                set = new HashSet<>();
                mediacodeMap.put(mediacode, set);
            }
            set.add(key);
        }
        for (Map.Entry<String, Set<String>> entry : mediacodeMap.entrySet()) {
            Map item = new HashMap();

            List<Map> values = new ArrayList<>();
            for (String key : entry.getValue()) {
                //每个mediacode
                String count = redisCommonDAO.get(key);
                if (StringUtils.isBlank(count)) {
                    continue;
                }
                Map value = new HashMap();
                value.put("sysid", key.substring(key.length() - 1));
                value.put("value", count);
                values.add(value);
            }
            item.put("media", entry.getKey());
            item.put("values", values);
            medias.add(item);
        }
        if(!CollectionUtils.isEmpty(medias)){
            redisCommonDAO.set(viewKey, JsonUtils.objectToJson(medias));
        }
    }

    @Override
    public String getActivityId() {
        try {
            String actiId = activityRepository.selectMaxActivityId();
            if(StringUtils.isBlank(actiId)){
                actiId = "001";
            }else if(actiId.length() <= 3 && actiId.startsWith("0")) {
                String nStr = actiId.substring(actiId.indexOf("0"));
                Integer n = Integer.parseInt(nStr);
                n++;
                String newV = n.toString();
                if (newV.length() == 1) {
                    newV = "00" + newV;
                }
                if (newV.length() == 2) {
                    newV = "0" + newV;
                }
                actiId = newV;
            }else{
                Integer n = Integer.parseInt(actiId);
                n++;
                actiId = n.toString();
            }
            return actiId;
        } catch (Exception e) {
            log.error("fail to get pk of activity", e);
        }
        return null;
    }

    private boolean needToGetCode(ActivityEntity activityEntity){
        if(activityEntity == null){
            return false;
        }
        if(!activityEntity.getStatusType().equals(ActivityStatusType.VALID)){
            return false;
        }
        return activityEntity.getExpireTime().getTime() > System.currentTimeMillis();
    }
}
