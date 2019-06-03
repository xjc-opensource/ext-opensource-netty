package ext.opensource.netty.server.mqtt.api;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.server.mqtt.MqttSession;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/


public interface MqttSessionService {
	/**
	 * 会话远端缓存
	 * @param cacheList
	 */
	void setRemoteSessionCache(CacheList<MqttSession> cacheList);
	
	/**
	 * 存储会话
	 * @param clientId
	 * @param sessionStore
	 */
	void put(String clientId, MqttSession sessionStore);

	/**
	 * 获取会话
	 * @param clientId
	 * @return
	 */
	MqttSession getSession(String clientId);

	/**
	 * clientId的会话是否存在
	 * @param clientId
	 * @return
	 */
	boolean containsKey(String clientId);

	/**
	 * 删除会话
	 * @param clientId
	 */
	void remove(String clientId);
	
	/**
	 * writeAndFlush
	 * @param clientId
	 * @param obj
	 */
	void writeAndFlush(String clientId, Object obj);
	
	/**
	 * isCleanSession
	 * @param clientId
	 * @return
	 */
	boolean isCleanSession(String clientId);
	
	/**
	 * closeSession
	 * @param clientId
	 */
	void closeSession(String clientId);
	
	/**
	 * getWillMessage
	 * @param clientId
	 * @return
	 */
	MqttPublishMessage getWillMessage(String clientId);
}
