package ext.opensource.netty.client.mqtt.api;

import ext.opensource.netty.client.mqtt.common.MessageData;
import ext.opensource.netty.common.api.GlobalUniqueIdSet;
import ext.opensource.netty.common.core.CacheList;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttConsumer extends GlobalUniqueIdSet {
	/**
	 * 自定义缓存
	 * @param msgList
	 */
	public void setCacheList(CacheList<MessageData> msgList);
	
	/**
	 * 订阅主题
	 * @param topic
	 * @param qosValue
	 */
	public void subscribe(String topic, int qosValue);
	
	/**
	 * 接收订阅主题消息
	 * @param listener
	 */
	public void setConsumerListener(MqttConsumerListener listener);
	

}
