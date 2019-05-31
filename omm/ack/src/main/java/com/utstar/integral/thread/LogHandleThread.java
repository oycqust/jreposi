package com.utstar.integral.thread;

import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.Constant;
import com.utstar.integral.bean.InputResult;
import com.utstar.integral.bean.UserViewLog;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * created by UTSC1244 at 2019/5/28 0028
 */
@Component
@Slf4j
public class LogHandleThread extends Thread {

    @Resource
    private RedisCommonDAO redisCommonDAO;
    @Resource
    private CommonService commonService;
    @Resource
    private Environment environment;

    @Override
    public void run() {

        if(!"true".equalsIgnoreCase(environment.getProperty("handle.up.log.flag","false"))){
            return;
        }
        boolean trueBoolean = false;
        boolean trueRedieBoolean = true;
        while(trueRedieBoolean) {
            try {
                boolean isExist = redisCommonDAO.exist(Constant.LOG_KEY);
                if (!isExist) {
                    Thread.sleep(60000);
                }else{
                    trueBoolean = true;
                    trueRedieBoolean = false;
                }
            }catch (InterruptedException e) {
                log.error("fail to sleep 60000 ms in LogHandleThread. ", e);
            }catch (Exception e){
                log.error("fail to get in LogHandleThread", e);
            }
        }
        while (trueBoolean){
            boolean isSleep = true;
            try {
                Long llen = redisCommonDAO.llen(Constant.LOG_KEY);
                long start = System.currentTimeMillis();
                //itemRuleService.handleUserViewLogs(inputResult);

                if(llen > 0){
                    isSleep = false;
                    log.info("LogHandleThread handle log start.");
                    commonService.handleLog(llen);
                    long end = System.currentTimeMillis();
                    log.info("LogHandleThread handle log end, spend {} ms.",(end-start));
                }
            } catch (Exception e) {
                log.error("LogHandleThread failed. ",e);
            }finally {
                if(isSleep){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        log.error("LogHandleThread sleep 5000 ms failed. ",e);
                    }
                }
            }

        }

    }
}
