package ext.opensource.netty.common.api;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface GlobalUniqueIdSet {
	/**
	 * Custom set GlobalUniqueId object
	 * @param globalUniquedId
	 */
	public void setGlobalUniqueId(GlobalUniqueId globalUniquedId);
	
	/**
	 * 
	 * @param cacheList
	 */
	public void setGlobalUniqueIdCache(CacheList<UniqueIdInteger> cacheList);
}
