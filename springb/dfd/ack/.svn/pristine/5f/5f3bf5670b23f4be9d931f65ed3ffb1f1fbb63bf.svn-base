package com.utstar.integral.controller;

import com.alibaba.fastjson.JSONObject;
import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.service.CommonService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author UTSC0928
 * @date 2018/5/31
 */
@Controller
@RequestMapping(value = "/match")
public class MatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

    @Resource
    private CommonService commonService;

    @Resource
    private RedisCommonDAO redisCommonDAO;

    @RequestMapping(value = "/reload")
    @ResponseBody
    public Map reloadMediacodeRedis(){
        Map<String,Object> result = new HashMap<>();
        String code = "0";
        long start = System.currentTimeMillis();
//        commonService.loadMediacodeToRedis();
        long end = System.currentTimeMillis();
        result.put("code",code);
        result.put("msg","reload redis mediacode data spend "+(end-start)+" ms");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/draw")
    public Map draw(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String userId = "";
        try {
            String info = IOUtils.toString(request.getInputStream());
            Map map = JSONObject.parseObject(info, Map.class);
            if(map != null && map.get("userId") != null){
                userId = (String) map.get("userId");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("cannot read userId from request.");
            result.put("userId",userId);
            return result;
        }


        result.put("userId",userId);

//        String str = redisCommonDAO.hget(RedisKeyConstant.WORLDMATCH_USER_SECOND_TABLE, userId);
//        Long seconds = str == null?0L: Long.valueOf(str);
//        result.put("seconds",seconds);

        //超过60分钟则抽奖次数为1
//        result.put("times",seconds>=3600?1:0);

        return result;
    }
}
