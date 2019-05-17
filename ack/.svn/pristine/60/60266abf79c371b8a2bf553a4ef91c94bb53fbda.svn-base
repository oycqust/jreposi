package com.utstar.integral.service;

import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.InputResult;
import com.utstar.integral.bean.ItemBean;
import com.utstar.integral.bean.UserViewLog;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 根据积分项的规则处理InputResult
 * @author UTSC0928
 * @date 2018/6/4
 */
@Component
public class ItemRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRuleService.class);

    private static final Logger WORLD_LOGGER = LoggerFactory.getLogger("WORLD");
    private static final Logger HERO_LOGGER = LoggerFactory.getLogger("HERO");
    private static final Logger SUMMER_LOGGER = LoggerFactory.getLogger("SUMMER");
    private static final Logger VOTEING_LOGGER = LoggerFactory.getLogger("VOTEING");

    @Resource
    private RedisCommonDAO redisCommonDAO;

    /**
     * 按照积分项规则处理inputResult数据
     * @param inputResult
     */
    public void handleUserViewLogs(InputResult inputResult){
        List<UserViewLog> userViewLogs = inputResult.getUserViewLogs();
        String cdrTime = inputResult.getCdrTime();

        //世界杯的mediacode集合
        Set<String> mediacodeSetWorld = redisCommonDAO.sMembers(RedisKeyConstant.INTEGRAL_MEDIACODE_WORLD);
        //对应004活动中视频观看次数code
        Set<String> mediacodeSetVoting = redisCommonDAO.hgetAllKey(RedisKeyConstant.INTEGRAL_MEDIACODE_VOTING);
        String sysid="";
        for(ItemBean itemBean:ItemBean.itemBeanList) {
            //电竞投票的活动
            //对应004编排下面的syside(因为各个运营商的编排不一样)
            if (ItemBean.VOTING.equals(itemBean.getItemId())) {
                sysid = itemBean.getSysids().get(0);
                break;
            }
        }
        Set<String>  categoryMediacodeSetVoting = redisCommonDAO.sMembers(RedisKeyConstant.getVOTINGItemKey(sysid));

        Map<String,Long> worldMatchResult = new HashMap<>();
        Map<String,Long> heroCardResult = new HashMap<>();
        Map<String,Long> summerResult = new HashMap<>();
        //投票004中的观看次数
        Map<String,Long> votingTemp = new HashMap<>();
        Map<String,Map<String,Long>> votingResult = new HashMap<String,Map<String,Long>>();
        //投票004中编排
        Map<String,Long> votingCategoryResult = new HashMap<>();

        userViewLogs.forEach(userViewLog -> {

            String userid = userViewLog.getUserid();
            String mediacode = userViewLog.getMediacode();
            String type = userViewLog.getType();
            Long second = userViewLog.getSecond();
            String parentobject = userViewLog.getParentobject();

            ItemBean.itemBeanList.forEach(itemBean -> {

                if(ItemBean.WORLD_MATCH.equals(itemBean.getItemId())){

                    //必须是点播且在提供的影片范围内
                    if(mediacode != null && mediacodeSetWorld.contains(mediacode)){
                        //形如 acp_userid:123456:itemid:001
                        String userWorldMatchKey = RedisKeyConstant.getUserItemKey(userid, itemBean.getItemId());

                        long oldSecond = worldMatchResult.getOrDefault(userWorldMatchKey, 0L);
                        worldMatchResult.put(userWorldMatchKey, oldSecond+second);
                   }
               }else if(ItemBean.HERO_CARD.equals(itemBean.getItemId())){

                    //要在提供的影片范围内
                    if(mediacode != null && itemBean.getMediacodeSet().contains(mediacode)){
                        //形如 acp_userid:123456:itemid:002:subitemid:02000001000000050000000000000008
                        String userHeroCardKey = RedisKeyConstant.getUserItemSubitemKey(userid, itemBean.getItemId(), mediacode);

                        long oldSecond = heroCardResult.getOrDefault(userHeroCardKey,0L);
                        heroCardResult.put(userHeroCardKey,oldSecond+second);
                   }
               }else if(ItemBean.SUMMER.equals(itemBean.getItemId())){
                   //当status=v, seriesFlag=1 的时候，parentobject就是连续剧剧头。
                   //status=type
                   //要在提供的影片范围内
                    if("v".equals(type)&&"1".equals(userViewLog.getSeriesFlag())){
                        if(parentobject != null && itemBean.getMediacodeSet().contains(parentobject)){
                            //形如 acp_userid:123456:itemid:003:subitemid:02000001000000050000000000000008
                            String userItemSubitemKey = RedisKeyConstant.getUserItemSubitemKey(userid, itemBean.getItemId(), parentobject);

                            Long oldSecond = summerResult.getOrDefault(userItemSubitemKey, 0L);
                            summerResult.put(userItemSubitemKey,oldSecond+second);
                        }
                    }
                }else if(ItemBean.VOTING.equals(itemBean.getItemId())){
                    //电竞投票的活动
                    //对应code观看次数在提供的影片范围内
                    if(mediacode != null && mediacodeSetVoting.contains(mediacode)){
                        //形如 acp_userid:123456:itemid:004:watch
                        String userVotingMatchKey = RedisKeyConstant.getVotingItemKey(userid, itemBean.getItemId(),0);
                        long oldSecond = 0L;
                        long tempSecond = 0L;
                        if(redisCommonDAO.hgetValue(userVotingMatchKey, mediacode)!=null&&oldSecond==0) {
                            oldSecond =  Long.parseLong((String)redisCommonDAO.hgetValue(userVotingMatchKey, mediacode));
                        }

                        if(second>Integer.parseInt(itemBean.getSecViewSecond())){
                            tempSecond++;
                        }
                        if(votingTemp.containsKey(userVotingMatchKey+"@"+mediacode)){
                            oldSecond = votingTemp.get(userVotingMatchKey+"@"+mediacode);
                        }
                        votingTemp.put(userVotingMatchKey+"@"+mediacode, oldSecond+tempSecond);
                        votingResult.put(userVotingMatchKey+"@"+mediacode, votingTemp);
                    }

                    if(mediacode != null && categoryMediacodeSetVoting.contains(mediacode)){
                        //形如 acp_userid:123456:itemid:004:category
                        String userCategoryVotingMatchKey = RedisKeyConstant.getVotingItemKey(userid, itemBean.getItemId(),1);
                        long oldSecond = votingCategoryResult.getOrDefault(userCategoryVotingMatchKey, 0L);
                        votingCategoryResult.put(userCategoryVotingMatchKey, oldSecond+second);
                    }
                }
            });

        });

        //存入redis
        if(worldMatchResult.size() > 0){
            redisCommonDAO.multiIncr(worldMatchResult);
            LOGGER.info("{} keys for item {} update....",worldMatchResult.size(),ItemBean.WORLD_MATCH);

            //存入日志
            worldMatchResult.forEach((key,second)->{
                String userid = key.split(":")[1];
                if(!StringUtils.isEmpty(cdrTime)){
                    WORLD_LOGGER.info("{},{},{}",cdrTime,userid,second);
                }else{
                    WORLD_LOGGER.info("{},{}",userid,second);
                }
            });
        }
        if(heroCardResult.size() > 0){
            redisCommonDAO.multiIncr(heroCardResult);
            LOGGER.info("{} keys for item {} update....",heroCardResult.size(),ItemBean.HERO_CARD);

            //存入日志
            heroCardResult.forEach((key, second) -> {
                String userid = key.split(":")[1];
                String mediacode = key.split(":")[5];
                if(!StringUtils.isEmpty(cdrTime)){
                    HERO_LOGGER.info("{},{},{},{}",cdrTime,userid,mediacode,second);
                }else{
                    HERO_LOGGER.info("{},{},{}",userid,mediacode,second);
                }
            });
        }
        if(summerResult.size() > 0){
            redisCommonDAO.multiIncr(summerResult);
            LOGGER.info("{} keys for item {} update....",summerResult.size(),ItemBean.SUMMER);

            //存入日志
            summerResult.forEach((key,second) -> {
                String userid = key.split(":")[1];
                String mediacode = key.split(":")[5];
                if(!StringUtils.isEmpty(cdrTime)){
                    SUMMER_LOGGER.info("{},{},{},{}",cdrTime,userid,mediacode,second);
                }else{
                    SUMMER_LOGGER.info("{},{},{}",userid,mediacode,second);
                }
            });
        }
        if(votingResult.size() > 0||votingCategoryResult.size()>0){
            if(votingCategoryResult.size()>0) {
                redisCommonDAO.multiIncr(votingCategoryResult);
            }
            if(votingResult.size()>0){
                for (Map.Entry<String, Map<String,Long>> entry : votingResult.entrySet()) {
                    String key = entry.getKey().split("@")[0];
                    for (Map.Entry<String,Long> valueEntry : entry.getValue().entrySet()) {
                        if(key.equals(valueEntry.getKey().split("@")[0])) {
                            redisCommonDAO.hsetnx(key, valueEntry.getKey().split("@")[1], String.valueOf(valueEntry.getValue()));
                        }
                    }
                }
            }
            LOGGER.info("{} keys for item {} update....",(votingResult.size()+votingCategoryResult.size()),ItemBean.VOTING);

            //存入日志
            votingCategoryResult.forEach((key,second) -> {
                String userid = key.split(":")[1];
                if(!StringUtils.isEmpty(cdrTime)){
                    VOTEING_LOGGER.info("{},{},{}",cdrTime,userid,second);
                }else{
                    VOTEING_LOGGER.info("{},{}",userid,second);
                }
            });
        }


        //如果是文件系统则cdrTime不为空
        if(!StringUtils.isEmpty(cdrTime)){
            redisCommonDAO.set(RedisKeyConstant.INTEGRAL_CDR_TIME,cdrTime);
            LOGGER.info("{} change to {}",RedisKeyConstant.INTEGRAL_CDR_TIME,cdrTime);
        }
    }
}
