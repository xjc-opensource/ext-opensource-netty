package ext.opensource.netty.client.mqtt.api;

import ext.opensource.netty.client.mqtt.common.MessageData;
import ext.opensource.netty.common.api.GlobalUniqueIdSet;
import ext.opensource.netty.common.core.CacheList;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface MqttProducer extends GlobalUniqueIdSet {
	/**
	 * 自定义缓存
	 * @param msgList
	 */
	void setCacheList(CacheList<MessageData> msgList);
	
	
	/**
	 * 发布消息
	 * @param topic
	 * @param message
	 * @param qosValue
	 * @param isRetain
	 */
	void sendPubishMessage(String topic, String message, int qosValue, boolean isRetain);
	

}
