package com.utstar.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 *
 * @author UTSC0928
 * @author UTSC0928
 * @date 2018/5/30
 */
@SpringBootConfiguration
@Slf4j
public class SpringConfig {

    @Resource
    private Environment environment;

    @Bean
    public JdkSerializationRedisSerializer getJdkSerializationRedisSerializer(){
        return new JdkSerializationRedisSerializer();
    }

    @Bean
    public StringRedisSerializer getStringRedisSerializer(){
        return new StringRedisSerializer();
    }

    @Bean("commonRedisTemplate")
    public RedisTemplate<String,Object> getCommonRedisTemplate(RedisConnectionFactory redisConnectionFactory, StringRedisSerializer stringRedisSerializer, JdkSerializationRedisSerializer jdkSerializationRedisSerializer){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public SelfConfigReader getSelfConfigReader(){
        SelfConfigReader selfConfigReader = new SelfConfigReader();
        String dataDir = environment.getProperty("data.dir","/opt/spark/sparkStreaming/data/viewlog").trim();
        log.info("data.dir is {}", dataDir);
        selfConfigReader.setDataDir(dataDir.trim());
        selfConfigReader.setSchedulePoolSize(Integer.valueOf(environment.getProperty("schedule.pool.size","10").trim()));
        return selfConfigReader;
    }

    /**
     * 配置spring定时任务的线程池
     * @param selfConfigReader
     * @return
     */
    @Bean
    public SchedulingConfigurer getSchedulingConfigurer(SelfConfigReader selfConfigReader){
        return taskRegistrar -> {
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(selfConfigReader.getSchedulePoolSize(),
                    new BasicThreadFactory.Builder().namingPattern("acp-schedule-pool-%d").daemon(true).build());
            taskRegistrar.setScheduler(executorService);
            /**
             * 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。 说明：Executors各个方法的弊端：
             1）newFixedThreadPool和newSingleThreadExecutor:
               主要问题是堆积的请求处理队列可能会耗费非常大的内存，甚至OOM。
             2）newCachedThreadPool和newScheduledThreadPool:
               主要问题是线程数最大数是Integer.MAX_VALUE，可能会创建数量非常多的线程，甚至OOM。
             */
//                taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
        };
    }

}
