package com.utstar.integral.thread;

import com.utstar.integral.bean.ActivityEntity;
import com.utstar.integral.service.ActivityService;
import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * created by UTSC1244 at 2019/5/27 0027
 */
@Component
public class ThreadPoolManage {

    @Autowired
    private Environment environment;

    @Value("${threadPool.corePoolSize}")
    private int corePoolSize;

    @Value("${threadPool.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${threadPool.keepAliveTime}")
    private long keepAliveTime;

    @Value("${threadPool.workQueue.capacity}")
    private Integer capacity;

    private TimeUnit unit = TimeUnit.SECONDS;

    private BlockingQueue<Runnable> workQueue;

    public static List<ActivityEntity> cacheList = new ArrayList();

    private ThreadPoolExecutor threadPoolExecutor;

    public void init(){
        workQueue = new ArrayBlockingQueue<Runnable>(capacity);
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue);
    }

    public synchronized ThreadPoolExecutor getThreadPoolExecutor(){
        if(threadPoolExecutor == null){
            init();
        }
        return threadPoolExecutor;
    }
}
