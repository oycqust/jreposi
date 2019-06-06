package com.utstar.integral.thread;

import com.utstar.integral.bean.CaculateEntity;
import com.utstar.integral.bean.CaculateQueueManage;
import com.utstar.integral.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by UTSC1244 at 2019/6/6 0006
 */
@Slf4j
@Component
public class StoreCacuResultThread extends Thread{

    @Resource
    private CaculateQueueManage caculateQueueManage;
    @Resource
    private CalculateService calculateService;

    @Override
    public void run() {
        ArrayBlockingQueue<CaculateEntity> mvQueue = caculateQueueManage.getMvQueue();
        ArrayBlockingQueue<CaculateEntity> upQueue = caculateQueueManage.getUpQueue();
        ArrayBlockingQueue<CaculateEntity> logQueue = caculateQueueManage.getLogQueue();
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        fixedPool.execute(()->{
            handleMv(mvQueue, upQueue);
        });
        fixedPool.execute(()->{
            handleUp(upQueue, logQueue);
        });
        fixedPool.execute(()->{
            handleLog(logQueue);
        });

        //handleUp(upQueue, logQueue);
        //handleLog(logQueue);
    }

    //@Sy
    private void handleMv(ArrayBlockingQueue<CaculateEntity> currentQueue,
                          ArrayBlockingQueue<CaculateEntity> nextQueue){
        CaculateEntity cacuEntity = null;
        boolean isHandMv = false;
        while (true){
            try {
                log.info("caculate-handle:handle mv start...");
                isHandMv = false;
                cacuEntity = currentQueue.take();
                    calculateService.multiIncrAndExpire(cacuEntity.getMvMap(), cacuEntity.getActivityEntity().getZeroDel());
                    isHandMv = true;
                    nextQueue.put(cacuEntity);
            } catch (InterruptedException e) {
                if(isHandMv){
                    try {
                        nextQueue.put(cacuEntity);
                    } catch (InterruptedException ex) {
                        log.error("caculate-handle:fail to put up from queue");
                    }
                }else{
                    log.error("caculate-handle:fail to take mv from queue");
                }
            }catch (Exception e){
                if(cacuEntity != null) {
                    try {
                        currentQueue.put(cacuEntity);
                        log.error("caculate-handle:success to put mv to queue");
                    } catch (InterruptedException ex) {
                        log.error("caculate-handle:fail to put mv to queue", e);
                    }
                }
            }
        }
    }

    //@Async
    private void handleUp(ArrayBlockingQueue<CaculateEntity> currentQueue,
                          ArrayBlockingQueue<CaculateEntity> nextQueue){
        CaculateEntity cacuEntity = null;
        boolean isHandUp = false;
        while (true){
            try {
                log.info("caculate-handle:handle up start...");
                isHandUp = false;
                cacuEntity = currentQueue.take();
                    calculateService.storeUp2Redis(cacuEntity.getUpMap(),
                            cacuEntity.getActivityEntity().getZeroDel(),
                            cacuEntity.getActivityEntity().getLimit());
                    isHandUp = true;
                    nextQueue.put(cacuEntity);
            } catch (InterruptedException e) {
                if(isHandUp){
                    try {
                        nextQueue.put(cacuEntity);
                    } catch (InterruptedException ex) {
                        log.error("caculate-handle:fail to put log from queue");
                    }
                }else{
                    log.error("caculate-handle:fail to take mv from queue");
                }
            }catch (Exception e){
                if(cacuEntity != null) {
                    try {
                        currentQueue.put(cacuEntity);
                        log.error("caculate-handle:success to put up to queue");
                    } catch (InterruptedException ex) {
                        log.error("caculate-handle:fail to put up to queue", e);
                    }
                }
            }
        }
    }

    @Async
    private void handleLog(ArrayBlockingQueue<CaculateEntity> currentQueue){
        CaculateEntity cacuEntity = null;
        while (true){
            try {
                log.info("caculate-handle:handle log start...");
                cacuEntity = currentQueue.take();
                calculateService.storeLog2Redis(cacuEntity.getUpMap(),
                        cacuEntity.getActivityEntity().getActivityId());
            } catch (InterruptedException e) {
                log.error("caculate-handle:fail to take log from queue");
            }catch (Exception e){
                if(cacuEntity != null) {
                    try {
                        currentQueue.put(cacuEntity);
                        log.error("caculate-handle:success to put log to queue");
                    } catch (InterruptedException ex) {
                        log.error("caculate-handle:fail to put log to queue", e);
                    }
                }
            }
        }
    }
}
