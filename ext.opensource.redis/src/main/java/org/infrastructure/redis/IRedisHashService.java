package org.infrastructure.redis;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface IRedisHashService<V>  {
	
	/**
	 * setValueClass
	 * @param clz
	 */
	public void setValueClass(Class<V> clz);
	
	
	/**
	 * hSet
	 * @param key
	 * @param field
	 * @param obj
	 * @return
	 */
	public Boolean hSet(final String key, final String field, V obj);

	/**
	 * hGet
	 * @param key
	 * @param field
	 * @return
	 */
    public V hGet(final String key, final String field);

    /**
     * hDel
     * @param key
     * @param field
     */
    public void hDel(final String key, final String field);
    
    /**
     * hExists
     * @param key
     * @param field
     * @return
     */
    public boolean hExists(final String key, final String field);
    
    /**
     * hLen
     * @param key
     * @return
     */
    public long hLen(final String key);
   
    /**
     * hIncrBy
     * @param key
     * @param field
     * @param number
     * @return
     */
    public long hIncrBy(final String key, final String field, final long number);
    
    /**
     * incr
     * @param key
     * @return
     */
    public long incr(final String key);
    
    /**
     * delKey
     * @param key
     * @return
     */
    public boolean delKey(final String key);
}
