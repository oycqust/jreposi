package com.utstar.integral.redis.dao.imp;

import com.utstar.integral.redis.dao.RedisLockDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author UTSC0928
 * @date 2018/6/5
 */
@Component("redisLockDAO")
@Slf4j
public class RedisLockDAOImpl implements RedisLockDAO{

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public boolean isSameLocker(String lockKey, String requestId, int expireTime) {
        List<String> valueList = new ArrayList<String>();
        Jedis jedis = getJedis();
        try {
            valueList.add(requestId);
            valueList.add(Integer.valueOf(expireTime).toString());
            String tryScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end";
            Object eval = jedis.eval(tryScript, Collections.singletonList(lockKey), valueList);
            if("1".equals(eval.toString())){
                log.info("{} 重入锁 {} 成功",requestId,lockKey);
                return true;
            }
            return false;
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    @Override
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis jedis = getJedis();
        try {
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    @Override
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedis = getJedis();
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    @Override
    public Jedis getJedis() {
        Field jedisField = ReflectionUtils.findField(JedisConnection.class, "jedis");
        ReflectionUtils.makeAccessible(jedisField);
        Jedis jedis = (Jedis) ReflectionUtils.getField(jedisField, redisConnectionFactory.getConnection());
        return jedis;
    }
}
