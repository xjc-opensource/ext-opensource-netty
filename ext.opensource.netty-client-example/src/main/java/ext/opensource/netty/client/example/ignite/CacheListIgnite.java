package ext.opensource.netty.client.example.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import ext.opensource.netty.common.core.BaseCacheList;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class CacheListIgnite<T> extends BaseCacheList<T> {
	private IgniteCache<String, T> msgList;

	public void setMsgList(IgniteCache<String, ?> msgList) {
	}
	
	public CacheListIgnite(String cacheName, Ignite ignite) {
		super(cacheName);
		CacheConfiguration<String, T> cacheConfiguration = new CacheConfiguration<String, T>();
		cacheConfiguration.setDataRegionName("persistence-data-region").setCacheMode(CacheMode.LOCAL)
				.setName(getCacheName());
		this.msgList = (IgniteCache<String, T>) ignite.getOrCreateCache(cacheConfiguration);
	}

	@Override
	public boolean put(String key, T value) {
		msgList.put(key, value);
		return true;
	}

	@Override
	public T get(String key) {
		if (msgList.containsKey(key)) {
			return msgList.get(key);
		} else {
			return null;
		}
	}

	@Override
	public T remove(String key) {
		return msgList.getAndRemove(key);
	}

	@Override
	public long size() {
		return msgList.size();
	}

	@Override
	public boolean exists(String key) {
		return msgList.containsKey(key);
	}
}
