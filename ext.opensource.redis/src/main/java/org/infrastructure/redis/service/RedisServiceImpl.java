package org.infrastructure.redis.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.infrastructure.redis.IRedisService;
import org.infrastructure.redis.core.JosnRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    @Override
    public boolean set(final String key, final String value) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    @Override
    public boolean set(String key, String value, long expireSecond) {
        boolean flag = set(key, value);
        if (flag) {
            flag = expire(key, expireSecond);
        }
        return flag;
    }

    @Override
    public String get(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

    @Override
    public Long incr(final String key) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.incr(serializer.serialize(key));
            }
        });
        return result;
    }

    @Override
    public boolean exists(final String key) {
        Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.exists(serializer.serialize(key));
            }
        });
        return result == null ? false : result;
    }

    @Override
    public boolean setObject(String key, Object obj) {
        final String value = JosnRedisUtil.toJson(obj);
        if ((value != null) && (value.length() > 0)) {
            return set(key, value);
        } else {
            return false;
        }
    }

    @Override
    public boolean setObject(String key, Object obj, long expireSecond) {
        boolean flag = setObject(key, obj);
        if (flag) {
            flag = expire(key, expireSecond);
        }
        return flag;
    }

    @Override
    public <T> T getObject(String key, Class<T> clz) {
        String value = get(key);
        if ((value != null) && (value.length() > 0)) {
            return JosnRedisUtil.toBean(value, clz);
        } else {
            return null;
        }
    }

    @Override
    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public <T> boolean setList(String key, List<T> list) {
        String value = JosnRedisUtil.toJson(list);
        return set(key, value);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clz) {
        String json = get(key);
        if (json != null) {
            List<T> list = JosnRedisUtil.toList(json, clz);
            return list;
        }
        return null;
    }

    @Override
    public long lpush(final String key, Object obj) {
        final String value = JosnRedisUtil.toJson(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    @Override
    public long rpush(final String key, Object obj) {
        final String value = JosnRedisUtil.toJson(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    @Override
    public String lpop(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] res = connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }

    @Override
    public Boolean hSet(final String key, final String field, Object obj) {
        final String value = JosnRedisUtil.toJson(obj);
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.hSet(serializer.serialize(key), serializer.serialize(field),
                        serializer.serialize(value));
            }
        });
    }

    @Override
    public <T> T hGet(final String key, final String field, Class<T> clz) {
        return redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection) {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] res = connection.hGet(serializer.serialize(key), serializer.serialize(field));
                if (res != null && res.length > 0) {
                    return JosnRedisUtil.toBean(serializer.deserialize(res), clz);
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public void delKey(final String key) {
        redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                redisTemplate.delete(key);
                return true;
            }
        });
    }

    /**
     * 获取redis的分布式锁，内部实现使用了redis的setnx。 此方法只执行一次, 获取成功返回true , 获取失败返回false
     * 
     * @param key
     *            要锁定的key
     * @param expireMillisecond
     *            超时时间 毫秒
     * @return boolean
     */
    @Override
    public boolean getLock(final String key, final long expireMillisecond) {
        final String value = key.hashCode() + "";
        
        String status = redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                String status = jedis.set(key, value, "nx", "px", expireMillisecond);
                return status;
            }
        });

        return "OK".equals(status) ? true : false;

    }

}
