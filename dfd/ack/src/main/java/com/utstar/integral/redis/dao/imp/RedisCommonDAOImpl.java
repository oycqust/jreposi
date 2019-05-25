package com.utstar.integral.redis.dao.imp;

import com.utstar.integral.redis.dao.RedisCommonDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author UTSC0928
 * @date 2018/3/8
 */
@Component("redisCommonDAO")
@Slf4j
public class RedisCommonDAOImpl<T> implements RedisCommonDAO<T> {


    @Resource(name = "commonRedisTemplate")
    protected RedisTemplate<String,Object> redisTemplate;

    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public Long incr(String key) {
        return stringRedisTemplate.opsForValue().increment("",1);
    }

    @Override
    public Long incr(String key, long step) {
        return stringRedisTemplate.opsForValue().increment("",step);
    }

    @Override
    public void multiIncr(Map<String,Long> map){
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                connection.openPipeline();

                map.forEach((key, second) -> {
                    connection.incrBy(keySerializer.serialize(key),second);

                });

                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public void multiHIncr(Map<String, Long> integerMap){
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
                RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
                connection.openPipeline();

//                byte[] hashKey = keySerializer.serialize(RedisKeyConstant.WORLDMATCH_USER_SECOND_TABLE);
//                integerMap.forEach((userId,seconds) -> {
//                    connection.hIncrBy(hashKey,keySerializer.serialize(userId),seconds);
//                });

                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public void multiHset(String key, Set<String> set){
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) redisTemplate.getValueSerializer();
                connection.openPipeline();

                set.forEach((str) -> {
                    connection.hSet(keySerializer.serialize(key), valueSerializer.serialize(str),valueSerializer.serialize("0"));
                });

                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public String hget(String key, String field) {
        return (String) stringRedisTemplate.opsForHash().get(key,field);
    }

    @Override
    public boolean exist(String key){
        return redisTemplate.hasKey(key);
    }

    @Override
    public void set(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long time, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    @Override
    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public List<T> multiGetObj(Collection<String> keys) {
        return (List<T>) redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public List<String> multiGet(Collection<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void multiSetObj(Map<String,T> dtoMap){
        redisTemplate.opsForValue().multiSet(dtoMap);
    }

    @Override
    public void multiSet(Map<String,String> dtoMap){
        redisTemplate.opsForValue().multiSet(dtoMap);
    }

    @Override
    public Long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public List<String> lrange(String key, long start, long end){
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public List<T> lrangeObj(String key, long start, long end) {
        return (List<T>) redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Map<String,List<String>> multiLrange(final Collection<String> keys){
        final Map<String,List<String>> result = new HashMap<String, List<String>>();

        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (String key : keys) {
                    byte[] bytes = keySerializer.serialize(key);
                    connection.lRange(bytes, 0, -1);
                }
                List<Object> objects = connection.closePipeline();
                int index=0;
                for (String key : keys) {
                    List list = (List) objects.get(index);
                    List<String> valueList = new ArrayList<String>();
                    for(int i=0; i<list.size(); i++){
                        String value = valueSerializer.deserialize((byte[]) list.get(i));
                        valueList.add(value);
                    }
                    index++;
                    result.put(key, valueList);
                }

                return null;
            }
        });
        return result;
    }

    @Override
    public void lpush(String key, String value){
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public String lpop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    @Override
    public void lpushAll(String key, Collection<String> values){
        stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public void rpushAll(String key, Collection<String> values) {
        stringRedisTemplate.opsForList().rightPushAll(key,values);
    }

    @Override
    public void lpushAllObj(String key, Collection<T> values) {
        redisTemplate.opsForList().leftPushAll(key,values);
    }

    @Override
    public void lrem(String key, String value){
        stringRedisTemplate.opsForList().remove(key, 0,value);
    }

    @Override
    public void multiLrem(final String key, final Collection<String> values){
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();
                byte[] nameBytes = keySerializer.serialize(key);

                for (String str : values) {
                    connection.lRem(nameBytes, 0,valueSerializer.serialize(str));
                }

                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public void multiLrem(final Map<String, Collection<String>> collectionMap) {
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (Map.Entry<String, Collection<String>> entry : collectionMap.entrySet()) {
                    byte[] nameBytes = keySerializer.serialize(entry.getKey());
                    Collection<String> collection = entry.getValue();
                    if(collection.size() > 0){
                        for (String str : collection) {
                            connection.lRem(nameBytes, 0,valueSerializer.serialize(str));
                        }
                    }
                }

                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public void multiLpushAll(final Map<String,Collection<String>> listMap){
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (Map.Entry<String,Collection<String>> entry : listMap.entrySet()) {
                    byte[] keyBytes = keySerializer.serialize(entry.getKey());
                    Collection<String> values = entry.getValue();
                    if(values.size() > 0){
                        byte[][] byteTwoArr = new byte[values.size()][];
                        int i=0;
                        for (String str : values) {
                            byte[] bytes = valueSerializer.serialize(str);
                            byteTwoArr[i++]=bytes;
                        }
                        connection.lPush(keyBytes,byteTwoArr);
                    }
                }
                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public Map<String,Integer> multiLlen(final Collection<String> keys) {
        final Map<String,Integer> result = new HashMap<String, Integer>();

        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (String key : keys) {
                    byte[] bytes = keySerializer.serialize(key);
                    connection.lLen(bytes);
                }
                List<Object> objects = connection.closePipeline();
                int index=0;
                for (String key : keys) {
                    Long length = (Long) objects.get(index);
                    index++;
                    result.put(key, length.intValue());
                }

                return null;
            }
        });
        return result;
    }

    @Override
    public void setObj(String key, T value){
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public T getObj(String key){
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void batchInsertT(final Map<String, T> dtoMap) {
        batchInsertT(dtoMap,null);
    }

    @Override
    public void batchInsertT(final Map<String, T> dtoMap, final Map<String,Long> expireMap) {
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
                RedisSerializer<Object> defaultSerializer = (RedisSerializer<Object>) redisTemplate.getDefaultSerializer();

                connection.openPipeline();
                for (Map.Entry<String, T> entry : dtoMap.entrySet()) {
                    String name = entry.getKey();
                    T value = entry.getValue();
                    byte[] nameBytes = keySerializer.serialize(name);
                    byte[] valueBytes = defaultSerializer.serialize(value);
                    connection.set(nameBytes,valueBytes);
                    if(expireMap != null){
                        Long expire = expireMap.get(name);
                        if(!(-1L == expire.longValue())){
                            connection.expire(nameBytes, expire);
                        }
                    }
                }
                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public void batchInsertStr(final Map<String, Collection<String>> listMap) {
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();

                connection.openPipeline();
                for (Map.Entry<String, Collection<String>> entry : listMap.entrySet()) {
                    String name = entry.getKey();
                    Collection<String> value = entry.getValue();

                    if(value.size() == 0){
                        continue;
                    }

                    byte[][] byteTwoArr = new byte[value.size()][];
                    int i=0;
                    for (String str : value) {
                        byteTwoArr[i++]=valueSerializer.serialize(str);
                    }

                    byte[] nameBytes = keySerializer.serialize(name);

                    connection.lPush(nameBytes,byteTwoArr);
                }
                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public void batchDelListStr(final Collection<String> collection, final String str) {
        stringRedisTemplate.execute(new RedisCallback<Long>(){

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                byte[] valueBytes = keySerializer.serialize(str);
                for (String key : collection) {
                    byte[] nameBytes = keySerializer.serialize(key);

                    connection.lRem(nameBytes,0L,valueBytes);
                }

                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public Set<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    @Override
    public void del(String key){
        redisTemplate.delete(key);
    }

    @Override
    public void del(Collection<String> keys){
        redisTemplate.delete(keys);
    }

    @Override
    public void sadd(String key, Collection<String> collection) {
        stringRedisTemplate.opsForSet().add(key,collection.toArray(new String[collection.size()]));
    }

    @Override
    public void multiSAdd(Map<String, Collection<String>> collectionMap) {
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (Map.Entry<String,Collection<String>> entry : collectionMap.entrySet()) {
                    byte[] keyBytes = keySerializer.serialize(entry.getKey());
                    Collection<String> values = entry.getValue();
                    if(values.size() > 0){
                        byte[][] byteTwoArr = new byte[values.size()][];
                        int i=0;
                        for (String obj : values) {
                            byte[] bytes = valueSerializer.serialize(obj);
                            byteTwoArr[i++]=bytes;
                        }
                        connection.sAdd(keyBytes,byteTwoArr);
                    }
                }
                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public Map<String, Set<String>> multiSMembers(Collection<String> keys) {

        final Map<String,Set<String>> result = new HashMap<String, Set<String>>();

        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (String key : keys) {
                    byte[] bytes = keySerializer.serialize(key);
                    connection.sMembers(bytes);
                }
                List<Object> objects = connection.closePipeline();
                int index=0;
                for (String key : keys) {
                    Set<byte[]> bytes = (Set<byte[]>) objects.get(index);
                    Set<String> bytesStr = new HashSet<String>(bytes.size());
                    for (byte[] byteArr : bytes) {
                        bytesStr.add(valueSerializer.deserialize(byteArr));
                    }

                    index++;
                    result.put(key, bytesStr);
                }

                return null;
            }
        });
        return result;
    }

    @Override
    public Set<String> sMembers(String key){
        return stringRedisTemplate.opsForSet().members(key);
    }

    @Override
    public boolean sIsMember(String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public void multiSRem(Map<String, Collection<String>> collectionMap) {
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (Map.Entry<String,Collection<String>> entry : collectionMap.entrySet()) {
                    byte[] keyBytes = keySerializer.serialize(entry.getKey());
                    Collection<String> values = entry.getValue();
                    if(values.size() > 0){
                        byte[][] byteTwoArr = new byte[values.size()][];
                        int i=0;
                        for (String obj : values) {
                            byte[] bytes = valueSerializer.serialize(obj);
                            byteTwoArr[i++]=bytes;
                        }
                        connection.sRem(keyBytes,byteTwoArr);
                    }
                }
                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    public String bRPopLPush(long timeout, String source, String destination) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(source, destination, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void sUnionStore(String destination, Collection<String> keys) {
        stringRedisTemplate.opsForSet().unionAndStore(null,keys,destination);
    }

    @Override
    public Set<String> sUnion(Collection<String> keys) {
        return stringRedisTemplate.opsForSet().union(null,keys);
    }

    @Override
    public Map<String, Set<String>> multiSUnion(Map<String, Collection<String>> paramMap) {
        final Map<String,Set<String>> result = new HashMap<String, Set<String>>();

        stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> keySerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
                RedisSerializer<String> valueSerializer = (RedisSerializer<String>) stringRedisTemplate.getValueSerializer();
                connection.openPipeline();

                for (Map.Entry<String, Collection<String>> entry : paramMap.entrySet()) {
                    Collection<String> value = entry.getValue();

                    byte[][] byteTwoArr = new byte[value.size()][];
                    int i=0;
                    for (String obj : value) {
                        byte[] bytes = valueSerializer.serialize(obj);
                        byteTwoArr[i++]=bytes;
                    }
                    connection.sUnion(byteTwoArr);
                }


                List<Object> objects = connection.closePipeline();
                int index=0;
                for (Map.Entry<String, Collection<String>> entry : paramMap.entrySet()) {
                    Set<byte[]> bytes = (Set<byte[]>) objects.get(index++);
                    Set<String> bytesStr = new HashSet<>(bytes.size());
                    for (byte[] byteArr : bytes) {
                        bytesStr.add(valueSerializer.deserialize(byteArr));
                    }
                    result.put(entry.getKey(), bytesStr);
                }

                return null;
            }
        });
        return result;
    }

    @Override
    public void hsetnx(String key, String field, String value) {
        stringRedisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public Map<Object ,Object> hgetALL(String key) {
        Map<Object ,Object> map = new LinkedHashMap<Object ,Object>();
        map = stringRedisTemplate.opsForHash().entries(key);
        return map;
    }
    @Override
    public void hDelete(String key,String code) {
        stringRedisTemplate.opsForHash().delete(key,code);
        return ;
    }

    @Override
    public Set<Object> hgetAllKey(String key) {
        Set<Object> set = stringRedisTemplate.opsForHash().keys(key);
        return set;
    }

    @Override
    public Object hgetValue(String key,String hkey) {
        Object hvalue = stringRedisTemplate.opsForHash().get(key, hkey);
        return hvalue;
    }

    @Override
    public Long getExpiredTime(String key){
        return redisTemplate.getExpire(key);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit){
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Jedis getJedis() {
        Field jedisField = ReflectionUtils.findField(JedisConnection.class, "jedis");
        ReflectionUtils.makeAccessible(jedisField);
        Jedis jedis = (Jedis) ReflectionUtils.getField(jedisField, redisConnectionFactory.getConnection());
        return jedis;
    }

    @Override
    public boolean multiPipeHset(String key, Set<String> set) {
        Pipeline pipelined = null;
        try {
            pipelined = getJedis().pipelined();
            for(String keyV: set){
                pipelined.hset(key, keyV, "0");
            }
            pipelined.sync();
            return true;
        } catch (Exception e) {
            log.error("fail to multiHset", e);
            return false;
        }finally {
            if(pipelined != null){
                try {
                    pipelined.close();
                } catch (IOException e) {
                    log.error("fail to pipelined close", e);
                }
            }
        }
    }

    @Override
    public Set<String> multiHget(String key) {
        Set<String> keySet = Collections.EMPTY_SET;
        Pipeline pipelined = null;
        try {
            pipelined = getJedis().pipelined();
            Response<Map<String, String>> mapResponse = pipelined.hgetAll(key);
            Map<String, String> stringStringMap = mapResponse.get();
            if(!CollectionUtils.isEmpty(stringStringMap)){
                keySet = stringStringMap.keySet();
            }
        } catch (Exception e) {
            log.error("Fail to multiHget", e);
            throw e;
        } finally {
            if(pipelined != null){
                try {
                    pipelined.close();
                } catch (IOException e) {
                    log.error("fail to pipelined close", e);
                }
            }
        }
        return keySet;
    }

}

