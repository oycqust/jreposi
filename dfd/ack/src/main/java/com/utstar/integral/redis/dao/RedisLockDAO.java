package com.utstar.integral.redis.dao;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import redis.clients.jedis.Jedis;

/**
 *
 * @author UTSC0928
 * @date 2018/6/5
 */
public interface RedisLockDAO {

    public static final String LOCK_SUCCESS = "OK";
    public static final String SET_IF_NOT_EXIST = "NX";
    public static final String SET_WITH_EXPIRE_TIME = "EX";
    public static final Long RELEASE_SUCCESS = 1L;

    /**
     * 是否能够重入锁
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public boolean isSameLocker(String lockKey,String requestId,int expireTime);

    /**
     * 尝试获取分布式锁
     * @param lockKey 被鎖住的key
     * @param requestId 被鎖住的key的值
     * @param expireTime 被鎖住的key的過期時間
     * @return 获取锁成功则返回true否则返回false
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime);

    /**
     * 释放分布式锁 只有持有锁的客户端才能释放锁
     * @param lockKey 被鎖住的key
     * @param requestId 被鎖住的key的值
     * @return 释放锁成功则返回true否则返回false
     */
    public boolean releaseDistributedLock(String lockKey, String requestId);

    /**
     * 获取jedis连接,注意用完之后需要关闭
     * @return
     */
    public Jedis getJedis();
}
