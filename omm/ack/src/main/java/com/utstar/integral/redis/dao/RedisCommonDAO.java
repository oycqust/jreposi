package com.utstar.integral.redis.dao;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author UTSC0928
 * @date 2018/03/08
 *
 * Redis基础命令接口
 */
public interface RedisCommonDAO<T> {

    /**
     * 对key进行自增
     *
     * @param key
     * @return
     */
    public Long incr(String key);

    public Long incr(String key, long step);

    /**
     * 对批量的key进行自增
     *
     * @param map
     */
    public Map<String,Long> multiIncr(Map<String, Long> map);

    /**
     * 批量新增
     *
     * @param integerMap
     */
    public void multiHIncr(Map<String, Long> integerMap);

    public void multiHset(String key, Set<String> set);

    public String hget(String key, String field);

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public boolean exist(String key);

    /**
     * 设置key，value值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value);

    public void set(String key, String value, long time, TimeUnit timeUnit);
    /**
     * 获取key对应的值
     *
     * @param key
     * @return
     */
    public String get(String key);

    /**
     * 批量获取keys的值的集合(针对于对象)
     *
     * @param keys
     * @return
     */
    public List<T> multiGetObj(Collection<String> keys);

    /**
     * 批量获取keys的值的集合(针对于string)
     *
     * @param keys
     * @return
     */
    public List<String> multiGet(Collection<String> keys);

    /**
     * 批量设置多个key的值，dtoMap中的K代表key，V代表该key要存的值(针对于对象)
     *
     * @param dtoMap
     */
    public void multiSetObj(Map<String, T> dtoMap);

    /**
     * 批量设置多个key的值，dtoMap中的K代表key，V代表该key要存的值(针对于string)
     *
     * @param dtoMap
     */
    public void multiSet(Map<String, String> dtoMap);

    Long llen(String key);

    /**
     * 获取key对应的list数据
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end);

    /**
     * 获取key对应的list数据--对象类型
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<T> lrangeObj(String key, long start, long end);

    /**
     * 批量获取keys对应的list数据的集合
     *
     * @param keys
     * @return 返回一个map集合，map中的K代表key，map中的V代表该key对应的list数据
     */
    public Map<String, List<String>> multiLrange(Collection<String> keys);

    /**
     * 从左边往该key放入value值
     *
     * @param key
     * @param value
     */
    public void lpush(String key, String value);

    /**
     * 从左边弹出一个元素
     *
     * @param key
     * @return
     */
    public String lpop(String key);

    /**
     * 从左边往该key放入多个值values
     *
     * @param key
     * @param values
     */
    public void lpushAll(String key, Collection<String> values);

    /**
     * 从右边往该key放入多个值values
     *
     * @param key
     * @param values
     */
    public void rpushAll(String key, Collection<String> values);

    /**
     * 从左边往该key放入多个类型为T的值values
     *
     * @param key
     * @param values
     */
    public void lpushAllObj(String key, Collection<T> values);

    /**
     * 删除该key对应的list数据中所有的value值
     *
     * @param key
     * @param value
     */
    public void lrem(String key, String value);

    /**
     * 删除该key对应的list数据中所有的values中的值
     *
     * @param key
     * @param values
     */
    public void multiLrem(String key, Collection<String> values);

    /**
     * 批量删除多个key(key的类型是list)
     * collectionMap中的K代表该key，V代表需要该key需要删除的数据的集合
     *
     * @param collectionMap
     */
    public void multiLrem(Map<String, Collection<String>> collectionMap);

    /**
     * 批量存入多个list，listMap中K代表该key，V代表该key需要存入的list数据
     *
     * @param listMap
     */
    public void multiLpushAll(final Map<String, Collection<String>> listMap);

    /**
     * 批量查询keys中每个元素对应的list的长度
     *
     * @param keys
     */
    public Map<String, Integer> multiLlen(final Collection<String> keys);

    /**
     * 设置对象值
     *
     * @param key
     * @param value
     */
    public void setObj(String key, T value);

    /**
     * 获取对象值
     *
     * @param key
     * @return
     */
    public T getObj(String key);

    /**
     * 批量设置多个key的值，dtoMap中的K代表key，V代表该key要存的值(针对于对象)
     *
     * @param dtoMap
     */
    public void batchInsertT(final Map<String, T> dtoMap);

    /**
     * 批量设置多个key的值，dtoMap中的K代表key，V代表该key要存的值(针对于对象)
     * expireMap存放的是key的过期时间，如果是-1则是永久有效
     *
     * @param dtoMap
     * @param expireMap
     */
    public void batchInsertT(final Map<String, T> dtoMap, final Map<String, Long> expireMap);

    /**
     * 批量存入多个list，listMap中K代表该key，V代表该key需要存入的list数据
     *
     * @param listMap
     */
    public void batchInsertStr(final Map<String, Collection<String>> listMap);

    /**
     * 批量删除多个key中的所有和str相等的数据，key的类型是list，
     *
     * @param collection
     * @param str
     */
    public void batchDelListStr(final Collection<String> collection, String str);

    /**
     * 根据正则表达式获取符合pattern的key的集合
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern);

    /**
     * 删除单个key
     *
     * @param key
     */
    public void del(String key);

    /**
     * 删除多个keys
     *
     * @param keys
     */
    public void del(Collection<String> keys);

    /**
     * 将collection存入到key里
     *
     * @param key
     * @param collection
     */
    public void sadd(String key, Collection<String> collection);

    /**
     * 批量存入多个set，collectionMap中K代表该key，V代表该key需要存入的set数据
     *
     * @param collectionMap
     */
    public void multiSAdd(final Map<String, Collection<String>> collectionMap);

    /**
     * 批量获取多个set，collectionMap中K代表key的集合
     *
     * @param keys
     */
    public Map<String, Set<String>> multiSMembers(final Collection<String> keys);

    /**
     * 获取key的集合
     *
     * @param key
     * @return
     */
    public Set<String> sMembers(String key);

    /**
     * 判断value是否在key的集合内
     *
     * @param keyHGETALL
     * @param value
     * @return
     */
    public boolean sIsMember(String key, String value);

    /**
     * 批量删除多个set，collectionMap中K代表该key，V代表该key需要删除的set数据
     *
     * @param collectionMap
     */
    public void multiSRem(final Map<String, Collection<String>> collectionMap);

    /**
     * 在timeout秒内将source的最右边元素pop出来加到destination的最左边
     *
     * @param timeout
     * @param source
     * @param destination
     */
    public String bRPopLPush(long timeout, String source, String destination);

    /**
     * 这个命令类似于 SUNION 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集
     * 如果 destination 已经存在，则将其覆盖。
     *
     * @param destination
     * @param keys
     */
    public void sUnionStore(String destination, Collection<String> keys);

    /**
     * 并集操作
     *
     * @param keys
     * @return
     */
    public Set<String> sUnion(Collection<String> keys);


    public Map<String, Set<String>> multiSUnion(Map<String, Collection<String>> paramMap);

    /**
     * 操作hash类型
     *
     * @param key
     * @param field
     * @param value
     */

    public void hsetnx(String key, String field, String value);

    /**
     * 返回全部的hash的key跟value数据
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hgetALL(String key);

    /**
     * 删除
     *
     * @param key
     * @param code
     */
    public void hDelete(String key, String code);

    /**
     * 获取全部的key
     *
     * @param key
     */
    public Set<Object> hgetAllKey(String key);

    /**
     * 获取对应key的value值
     * @param key
     * @param hkey
     * @return
     */

    public Object hgetValue(String key,String hkey);

    public Long getExpiredTime(String key);

    public Boolean expire(String key, long timeout, TimeUnit unit);

    public Jedis getJedis();

    public boolean multiPipeHset(String key, Set<String> set);

    public Set<String> multiHget(String key);

    boolean multiAddSet(String key, Set<String> set);

    boolean syncMediacode(String key, Set<String> set);

    public List<String> multiGetForList(String key, int len);

    void multiLpush(String key, Collection<String> collection);
}