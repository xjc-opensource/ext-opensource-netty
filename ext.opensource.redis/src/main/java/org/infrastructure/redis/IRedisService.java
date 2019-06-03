package org.infrastructure.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface IRedisService {

	/**
	 * set
	 * @param key
	 * @param value
	 * @return
	 */
    public boolean set(String key, String value);

    /**
     * set
     * @param key
     * @param value
     * @param expireSecond
     * @return
     */
    public boolean set(String key, String value, long expireSecond);

    /**
     * setObject
     * @param key
     * @param value
     * @return
     */
    public boolean setObject(String key, Object value);

    /**
     * setObject
     * @param key
     * @param value
     * @param expireSecond
     * @return
     */
    public boolean setObject(String key, Object value, long expireSecond);

    /**
     * getObject
     * @param key
     * @param clz
     * @return
     */
    public <T> T getObject(String key, Class<T> clz);

    /**
     * get
     * @param key
     * @return
     */
    public String get(String key);

    /**
     * expire
     * @param key
     * @param expire
     * @return
     */
    public boolean expire(String key, long expire);

    /**
     * getExpire
     * @param key
     * @return
     */
    public long getExpire(String key);

    /**
     * getExpire
     * @param key
     * @param timeUnit
     * @return
     */
    public long getExpire(String key, TimeUnit timeUnit);

    /**
     * setList
     * @param key
     * @param list
     * @return
     */
    public <T> boolean setList(String key, List<T> list);

    /**
     * getList
     * @param key
     * @param clz
     * @return
     */
    public <T> List<T> getList(String key, Class<T> clz);

    /**
     * lpush
     * @param key
     * @param obj
     * @return
     */
    public long lpush(String key, Object obj);

    /**
     * rpush
     * @param key
     * @param obj
     * @return
     */
    public long rpush(String key, Object obj);

    /**
     * lpop
     * @param key
     * @return
     */
    public String lpop(String key);

    /**
     * incr
     * @param key
     * @return
     */
    public Long incr(final String key);

    /**
     * hSet
     * @param key
     * @param field
     * @param obj
     * @return
     */
    public Boolean hSet(final String key, final String field, Object obj);
    
    /**
     * hGet
     * @param key
     * @param field
     * @param clz
     * @return
     */
    public <T> T hGet(final String key, final String field, Class<T> clz);
    
    /**
     * exists
     * @param key
     * @return
     */
    public boolean exists(final String key);
    
    /**
     * delKey
     * @param key
     */
    public void delKey(final String key);
    
    /**
     * getLock
     * @param key
     * @param expireMillisecond
     * @return
     */
    public boolean getLock(String key, long expireMillisecond);
}
