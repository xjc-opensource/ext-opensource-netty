package ext.opensource.netty.server.mqtt.api;

import java.util.List;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.server.mqtt.common.RetainMessage;
import ext.opensource.netty.server.mqtt.common.SubscribeTopicInfo;
import ext.opensource.netty.server.mqtt.protocol.data.ClientTopic;
import ext.opensource.netty.server.mqtt.protocol.data.TopicData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/


public interface TopicService {

	/**
	 * 自定义主题缓存
	 * @param cacheList
	 */
	void setTopicList(CacheList<TopicData> cacheList);
	
	/**
	 * 自定义用户主题缓存
	 * @param cacheList
	 */
	void setClientTopicList(CacheList<ClientTopic> cacheList);
	
	
	/**
	 * 存储订阅
	 * @param topicFilter
	 * @param subscribeStore
	 * @return
	 */
	boolean put(String topicFilter, SubscribeTopicInfo subscribeStore);

	/**
	 * 删除订阅
	 * @param topicFilter
	 * @param clientId
	 */
	void remove(String topicFilter, String clientId);

	/**
	 * 删除clientId的订阅
	 * @param clientId
	 */
	void removeForClient(String clientId);

	/**
	 * 获取订阅集
	 * @param topic
	 * @return
	 */
	List<SubscribeTopicInfo> search(String topic);
	
	
	/**
	 * putRetainMessage
	 * @param topic
	 * @param retainMessageInfo
	 */
	void putRetainMessage(String topic, RetainMessage retainMessageInfo);
	
	/**
	 * delete retain message
	 * @param topic
	 */
	void removeRetainMessage(String topic);
	
	/**
	 * search retain message
	 * @param topicFilter
	 * @return
	 */
	List<RetainMessage> searchRetainMessage(String topicFilter);
}
