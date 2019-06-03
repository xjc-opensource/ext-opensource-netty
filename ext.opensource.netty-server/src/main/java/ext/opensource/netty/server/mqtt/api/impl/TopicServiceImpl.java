package ext.opensource.netty.server.mqtt.api.impl;

import java.util.ArrayList;
import java.util.List;
import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.CacheListLocalMemory;
import ext.opensource.netty.server.mqtt.api.TopicService;
import ext.opensource.netty.server.mqtt.common.RetainMessage;
import ext.opensource.netty.server.mqtt.common.SubscribeTopicInfo;
import ext.opensource.netty.server.mqtt.protocol.data.ClientTopic;
import ext.opensource.netty.server.mqtt.protocol.data.TopicData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class TopicServiceImpl implements TopicService {
	private CacheList<TopicData> grouptopicData;
	private CacheList<ClientTopic> clientTopicData;

	public TopicServiceImpl() {
		grouptopicData = new CacheListLocalMemory<TopicData>();
		clientTopicData = new CacheListLocalMemory<ClientTopic>();
	}

	@Override
	public void setTopicList(CacheList<TopicData> cacheList) {
		if (cacheList == null) {
			return;
		}
		this.grouptopicData = cacheList;
	}

	@Override
	public void setClientTopicList(CacheList<ClientTopic> cacheList) {
		if (cacheList == null) {
			return;
		}
		this.clientTopicData = cacheList;
	}

	@Override
	public boolean put(String topicFilter, SubscribeTopicInfo subscribeInfo) {
		if (topicFilter == null) {
			return false;
		}
		String clientId = subscribeInfo.getClientId();

		TopicData topic = grouptopicData.containsKey(topicFilter) ? grouptopicData.get(topicFilter)
				: null;
		
		if (topic == null) {
			topic = new TopicData(grouptopicData.getCacheName() + ":" + topicFilter);
		}
		
		topic.put(subscribeInfo.getClientId(), subscribeInfo);
		grouptopicData.put(topicFilter, topic);

		ClientTopic clientTopic = clientTopicData.containsKey(clientId) ? clientTopicData.get(clientId)
				: new ClientTopic();
		clientTopic.put(subscribeInfo.getTopicFilter(), "");
		clientTopicData.put(clientId, clientTopic);
		
		return true;
	}

	private void deleteTopicData(String topicFilter, String clientId) {
		if (grouptopicData.containsKey(topicFilter)) {
			TopicData topic = grouptopicData.get(topicFilter);
			if (topic.containsKey(clientId)) {
				topic.remove(clientId);
				grouptopicData.put(topicFilter, topic);
			}
		}
	}

	@Override
	public void remove(String topicFilter, String clientId) {
		deleteTopicData(topicFilter, clientId);

		if (clientTopicData.containsKey(clientId)) {
			ClientTopic clientTopic = clientTopicData.get(clientId);
			if (clientTopic.containsKey(topicFilter)) {
				clientTopic.remove(topicFilter);
			}
			clientTopicData.put(clientId, clientTopic);
		}

	}

	@Override
	public void removeForClient(String clientId) {
		if (clientTopicData.containsKey(clientId)) {
			ClientTopic clientTopic = clientTopicData.get(clientId);
			for (String topic : clientTopic.keys()) {
				deleteTopicData(topic, clientId);
			}
		}

		clientTopicData.remove(clientId);
	}

	@Override
	public List<SubscribeTopicInfo> search(String topic) {
		List<SubscribeTopicInfo> list = new ArrayList<SubscribeTopicInfo>();
		if (grouptopicData.containsKey(topic)) {
			TopicData topicObj = grouptopicData.get(topic);
			list.addAll(topicObj.values());
		}
		return list;
	}

	@Override
	public void putRetainMessage(String topic, RetainMessage retainMessageInfo) {
		TopicData topicData = grouptopicData.get(topic);
		if (topicData != null) {
			topicData.setRetainMessageInfo(retainMessageInfo);
			grouptopicData.put(topic, topicData);
		}
	}
	
	@Override
	public void removeRetainMessage(String topic) {
		TopicData topicData = grouptopicData.get(topic);
		if (topicData != null) {
			topicData.setRetainMessageInfo(null);
			grouptopicData.put(topic, topicData);
		}
	}

	@Override
	public List<RetainMessage> searchRetainMessage(String topicFilter) {
		TopicData topicData = grouptopicData.get(topicFilter);
		RetainMessage retainInfo = topicData.getRetainMessageInfo();
		List<RetainMessage> list = new ArrayList<RetainMessage>();
		if (retainInfo != null) {
			list.add(retainInfo);
		}
		return list;
	}
}
