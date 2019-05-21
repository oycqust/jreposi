package com.utstar.integral.controller;

import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.DetailBean;
import com.utstar.integral.bean.ItemBean;
import com.utstar.integral.bean.Items;
import com.utstar.integral.bean.ResultBean;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * @author UTSC0928
 * @date 2018/6/4
 */
@Controller
public class QueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

    @Resource
    private RedisCommonDAO redisCommonDAO;

    @RequestMapping(value = "/query_items")
    @ResponseBody
    public ResultBean queryItems(String userid, String itemid){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(ResultBean.OK);
        resultBean.setMsg("");

        if(StringUtils.isEmpty(userid) || StringUtils.isEmpty(itemid)){
            resultBean.setCode(ResultBean.ERROR);
            resultBean.setMsg("userid or itemid is not exist");
            return resultBean;
        }

        ItemBean itemBean = ItemBean.itemBeanMap.get(itemid);
        if(ItemBean.WORLD_MATCH.equals(itemid)){
            String second = redisCommonDAO.get(RedisKeyConstant.getUserItemKey(userid, itemid));

            second = second==null?"0":second;
            resultBean.setViewseconds(second);
            resultBean.setScore(Long.parseLong(second)>(Integer.parseInt(itemBean.getMinViewMinutes())*60)?"1":"0");
            return resultBean;
        }else if(ItemBean.HERO_CARD.equals(itemid)){
            Set<String> keys = redisCommonDAO.keys(RedisKeyConstant.getUserItemSubitemKey(userid, itemid, "*"));
            if(keys.size() == 0){
                resultBean.setScore("0");
                resultBean.setDetails(Collections.emptyList());
                return resultBean;
            }
            List<String> secondList = redisCommonDAO.multiGet(keys);
            int i=0;
            int total = 0;
            List<DetailBean> detailBeans = new ArrayList<>();
            for (String key : keys) {
                String second = secondList.get(i);

                DetailBean detailBean = new DetailBean();
                detailBean.setSubitemid(key.split(RedisKeyConstant.SUBITEMID+RedisKeyConstant.SEPARATOR)[1]);
                detailBean.setViewseconds(second);
                String score = (Long.parseLong(second) > (Integer.parseInt(itemBean.getMinViewMinutes()) * 60)) ? "1" : "0";
                if("1".equals(score)){
                    total++;
                }
                detailBean.setScore(score);
                detailBeans.add(detailBean);

                i++;
            }
            resultBean.setScore(String.valueOf(total));
            resultBean.setDetails(detailBeans);
            return resultBean;
        }else if(ItemBean.SUMMER.equals(itemid)){
            Set<String> keys = redisCommonDAO.keys(RedisKeyConstant.getUserItemSubitemKey(userid, itemid, "*"));
            if(keys.size() == 0){
                resultBean.setScore("0");
                resultBean.setDetails(Collections.emptyList());
                return resultBean;
            }
            List<String> secondList = redisCommonDAO.multiGet(keys);
            int i=0;
            int total = 0;
            List<DetailBean> detailBeans = new ArrayList<>();
            for (String key : keys) {
                String second = secondList.get(i);

                DetailBean detailBean = new DetailBean();
                detailBean.setSubitemid(key.split(RedisKeyConstant.SUBITEMID+RedisKeyConstant.SEPARATOR)[1]);
                detailBean.setViewseconds(second);
//                String score = (Long.parseLong(second) > (Integer.parseInt(itemBean.getMinViewMinutes()) * 60)) ? "1" : "0";
                long coresMediacode = Long.parseLong(second) / (Integer.parseInt(itemBean.getMinViewMinutes()) * 60);
                coresMediacode=coresMediacode>=Integer.valueOf(itemBean.getMaxCoresPerDay())?Integer.valueOf(itemBean.getMaxCoresPerDay()):coresMediacode;
                total+=coresMediacode;
                detailBean.setScore(String.valueOf(coresMediacode));
                detailBeans.add(detailBean);

                i++;
            }
            resultBean.setScore(total>=Integer.valueOf(itemBean.getMaxCoresPerDay())?itemBean.getMaxCoresPerDay():String.valueOf(total));
            resultBean.setDetails(detailBeans);
            return resultBean;
        }

        return resultBean;
    }

    @RequestMapping(value = "/querytotal")
    @ResponseBody
    public ResultBean queryVotingTotal(String userid){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(ResultBean.OK);
        resultBean.setMsg("");

        if(StringUtils.isEmpty(userid)){
            resultBean.setCode(ResultBean.ERROR);
            resultBean.setMsg("userid  必须填写");
            return resultBean;
        }
        ItemBean itemBean = ItemBean.itemBeanMap.get("004");
        String second = redisCommonDAO.get(RedisKeyConstant.getVotingItemKey(userid, "004",1));
        second = second==null?"0":second;
        int total = Integer.parseInt(String.valueOf(Long.parseLong(second)/(Integer.parseInt(itemBean.getMinViewMinutes())*60)));
        resultBean.setTotal((total*5)>(Integer.parseInt(itemBean.getMaxCoresPerDay()))?(Integer.parseInt(itemBean.getMaxCoresPerDay())):(total*5));
        return resultBean;
    }

    @RequestMapping(value = "/queryitems")
    @ResponseBody
    public ResultBean queryVotingWatch(String userid, @RequestParam(value="mediacodelist",required=false)String codes ){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(ResultBean.OK);
        resultBean.setMsg("");
        if(StringUtils.isEmpty(userid)){
            resultBean.setCode(ResultBean.ERROR);
            resultBean.setMsg("userid  必须填写");
            return resultBean;
        }
        List<Items> itemsList = new ArrayList<>();
        ItemBean itemBean = ItemBean.itemBeanMap.get("004");
        Map<String, String> sourceMap = redisCommonDAO.hgetALL(RedisKeyConstant.getVotingItemKey(userid, "004",0));
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            if (!StringUtils.isEmpty(codes) && !"null".equals(codes) &&!(codes.contains(entry.getKey()))) {
                    continue;
            }
            Items items = new Items();
            String second = String.valueOf(entry.getValue());
            second = second==null?"0":second;
            items.setItemid(entry.getKey());
            items.setValue(Integer.parseInt(String.valueOf(Long.parseLong(second))));
            itemsList.add(items);

        }
        resultBean.setItems(itemsList);
        return resultBean;
    }
}
