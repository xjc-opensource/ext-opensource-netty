package ext.opensource.netty.common.api;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface GlobalUniqueId {
	
	void setCacheList(CacheList<UniqueIdInteger> cacheList);
	/**
	 * get uniuqe id
	 * @return
	 */
	int getNextMessageId(String clientId);
}
