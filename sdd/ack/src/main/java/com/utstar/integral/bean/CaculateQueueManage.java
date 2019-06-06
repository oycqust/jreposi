package com.utstar.integral.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * created by UTSC1244 at 2019/6/6
 */
@Component
@Data
public class CaculateQueueManage {

    private ArrayBlockingQueue<CaculateEntity> mvQueue = new ArrayBlockingQueue(Constant.MV_QUEUE_SIZE);

    private ArrayBlockingQueue<CaculateEntity> upQueue = new ArrayBlockingQueue(Constant.UP_QUEUE_SIZE);

    private ArrayBlockingQueue<CaculateEntity> logQueue = new ArrayBlockingQueue(Constant.LOG_QUEUE_SIZE);
}
