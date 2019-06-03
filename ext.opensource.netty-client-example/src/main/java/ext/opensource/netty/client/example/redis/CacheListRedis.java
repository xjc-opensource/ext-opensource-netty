package ext.opensource.netty.client.example.redis;


import org.infrastructure.redis.IRedisHashService;
import ext.opensource.netty.common.core.BaseCacheList;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@SuppressWarnings("rawtypes")
public class CacheListRedis<V> extends BaseCacheList<V> {
	private IRedisHashService<V> redisService;

	@SuppressWarnings("unchecked")
	public CacheListRedis(String cacheName, IRedisHashService redisService) {
		super(cacheName);
		this.redisService = redisService;
	}
	
	@Override
	public boolean put(String key, V value) {	
		return redisService.hSet(getCacheName(), key, value);
	}

	@Override
	public V get(String key) {
		return redisService.hGet(getCacheName(), key);
	}

	@Override
	public V remove(String key) {
		V value = get(key);
		redisService.hDel(getCacheName(), key);
		return value;
	}

	@Override
	public long size() {
		return redisService.hLen(getCacheName());
	}

	@Override
	public boolean exists(String key) {
		return redisService.hExists(getCacheName(), key);
	}
	
	@Override
	public boolean containsKey(String key) {
		return exists(key);
	}
}