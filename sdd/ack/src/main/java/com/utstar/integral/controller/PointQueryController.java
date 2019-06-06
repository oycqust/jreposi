package com.utstar.integral.controller;

import com.alibaba.fastjson.JSONArray;
import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.service.ActivityService;
import com.utstar.integral.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * created by UTSC1244 at 2019/5/24
 */
@RestController
@Slf4j
public class PointQueryController {
    @Resource
    private RedisCommonDAO redisCommonDAO;
    @Resource
    private ActivityService activityService;

    @RequestMapping("/pointquery")
    public Map<String, Object> pointQuery(@RequestParam(value = "userid", required = true) String userId,
                                          @RequestParam(value = "activityid", required = true) String activityId) {
        log.info("query point start. param: userId is {}, activityId is {}", userId, activityId);
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> values = new ArrayList<>();

        result.put("code", "0");
        result.put("msg", "success");
        result.put("activityid", activityId);
        result.put("userid", userId);
        result.put("values", values);

        try {
            String upkey = String.format(Constant.USER_POINT_FORMAT, activityId, userId, "*");
            Set<String> keys = redisCommonDAO.keys(upkey);
            if (!CollectionUtils.isEmpty(keys)) {
                for (String key : keys) {
                    String point = redisCommonDAO.get(key);
                    if (StringUtils.isNotBlank(point)) {
                        Map<String, Object> value = new HashMap<>();
                        value.put("sysid", key.substring(key.length() - 1));
                        value.put("value", point);
                        values.add(value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("fail to query point.", e);
            result.put("code", "-1");
        }

        log.info("query point end. param: userId is {}, activityId is {}", userId, activityId);
        return result;
    }

    @RequestMapping("/viewcountquery")
    @ResponseBody
    public Map<String, Object> viewCountQuery(@RequestParam(value = "mediacodelist", required = false) String mediacodeStr,
                                              @RequestParam(value = "activityid", required = true) String activityId) {
        log.info("query mediaViewCount start. param: mediacodelist is {}, activityId is {}", mediacodeStr, activityId);
        Map<String, Object> result = new HashMap<>();
        List<Map> medias = new ArrayList<>();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("activityid", activityId);
        result.put("medias", medias);

        try {
            if (StringUtils.isNotBlank(mediacodeStr)) {//查询指定媒资
                for (String mediacode : mediacodeStr.split(",")) {
                    String mvkey = String.format(Constant.MOVIE_VIEW_FORMAT, activityId, mediacode, "*");
                    Set<String> keys = redisCommonDAO.keys(mvkey);
                    if (CollectionUtils.isEmpty(keys)) {
                        continue;
                    }
                    Map item = new HashMap();
                    List<Map> values = new ArrayList<>();
                    for (String key : keys) {
                        String count = redisCommonDAO.get(key);
                        if (StringUtils.isBlank(count)) {
                            continue;
                        }
                        Map<String, Object> value = new HashMap<>();
                        value.put("sysid", key.substring(key.length() - 1));
                        value.put("value", count);
                        values.add(value);
                    }
                    item.put("media", mediacode);
                    item.put("values", values);
                    medias.add(item);
                }
            } else {//查询所有媒资
                String viewKey = String.format(Constant.MEDIA_VIEW_COUNT_FORMAT, activityId);
                if (redisCommonDAO.exist(viewKey)) {//从缓存中取
                    String viewTotalStr = redisCommonDAO.get(viewKey);
                    JSONArray jsonArray = JSONArray.parseArray(viewTotalStr);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String s = jsonArray.getJSONObject(i).toString();
                        Map maps = JsonUtils.jsonToPojo(s, Map.class);
                        medias.add(maps);
                    }
                } else {//从redis中取
                    activityService.sycnActiTotalCode(activityId, medias, viewKey);
                }
            }
            /*for(String sysid: sysids.split(",")) {
                String mvkey = String.format(Constant.MOVIE_VIEW_FORMAT, activityId, userViewLog.getMediacode(), userViewLog.getSysid());
                String point = redisCommonDAO.get(upkey);
                if(StringUtils.isNotBlank(point)){
                    Map<String, Object> value = new HashMap<>();
                    value.put("sysid", sysid);
                    value.put("value", point);
                    values.add(value);
                }
            }*/
        } catch (Exception e) {
            log.error("fail to query mediaViewCount.", e);
            result.put("code", "-1");
        }

        log.info("query mediaViewCount end. param: mediacodeList is {}, activityId is {}", mediacodeStr, activityId);
        return result;
    }


}
