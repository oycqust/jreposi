package com.utstar.integral.thread;

import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.InputResult;
import com.utstar.integral.bean.UserViewLog;
import com.utstar.integral.data.source.ViewLogInput;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.service.ItemRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 从数据源（File/Kafka）获取userviewlog数据，然后根据积分项的规则去处理数据存入到redis
 * @author UTSC0928
 * @date 2018/5/30
 */
@Component
public class IntegralWorkerThread extends Thread{

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegralWorkerThread.class);


    @Resource(name = "viewLogInputFile")
    private ViewLogInput viewLogInput;

    @Resource
    private ItemRuleService itemRuleService;

    @Resource
    private RedisCommonDAO redisCommonDAO;

    @Override
    public void run() {

        boolean trueBoolean = false;
        boolean trueRedieBoolean = true;
        while(trueRedieBoolean) {
            Set<String> mediacodeSetVoting = redisCommonDAO.hgetAllKey(RedisKeyConstant.INTEGRAL_MEDIACODE_VOTING);
            //第一次时候，客户还没上传对应观看影片的code，先让循环休眠1分钟。直到客户上传了code
            if (mediacodeSetVoting == null || mediacodeSetVoting.size() == 0) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    LOGGER.error("redis not 004 code sleep 60000 ms failed. ", e);
                }
            }else{
                trueBoolean = true;
                trueRedieBoolean = false;
            }
        }
        while (trueBoolean){
            boolean isSleep = true;
            try {
                long start = System.currentTimeMillis();
                InputResult inputResult = viewLogInput.getInputResult();
                List<UserViewLog> userViewLogs = inputResult.getUserViewLogs();

                itemRuleService.handleUserViewLogs(inputResult);

                if(userViewLogs.size() > 0){
                    isSleep = false;
                    long end = System.currentTimeMillis();
                    LOGGER.info("acquire and handle InputResult spend {} ms, userViewLogs length is {} \n",(end-start),userViewLogs.size());
                }
            } catch (Exception e) {
                LOGGER.error("IntegralWorkerThread failed. ",e);
            }finally {
                if(isSleep){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        LOGGER.error("IntegralWorkerThread sleep 5000 ms failed. ",e);
                    }
                }
            }

        }
    }

}
