package ext.opensource.netty.server.example.mqtt.redis;

import org.infrastructure.redis.IRedisHashService;
import org.springframework.beans.factory.annotation.Autowired;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.UniqueIdInteger;
import ext.opensource.netty.server.example.mqtt.MqttCustomCache;
import ext.opensource.netty.server.mqtt.MqttServer;
import ext.opensource.netty.server.mqtt.MqttSession;
import ext.opensource.netty.server.mqtt.api.CustomConfig;
import ext.opensource.netty.server.mqtt.protocol.data.ClientTopic;
import ext.opensource.netty.server.mqtt.protocol.data.ConsumerClientData;
import ext.opensource.netty.server.mqtt.protocol.data.ProcedureClientData;
import ext.opensource.netty.server.mqtt.protocol.data.TopicData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class CustomRedisCache extends MqttCustomCache {
	private IRedisHashService<TopicData> topicListService;
	private IRedisHashService<ClientTopic> clientTopicListService;
	private IRedisHashService<ConsumerClientData> consumerDataListService;
	private IRedisHashService<ProcedureClientData> procedureDataService;
	private IRedisHashService<MqttSession> remoteSessionListService;
	private IRedisHashService<UniqueIdInteger> uniqueIdListService;

	@Autowired
	private void setRedisTopicListService(
			IRedisHashService<TopicData> topicListService) {
		this.topicListService = topicListService;
		this.topicListService.setValueClass(TopicData.class);
	}

	@Autowired
	private void setClientTopicListService(
			IRedisHashService<ClientTopic> clientTopicListService) {
		this.clientTopicListService = clientTopicListService;
		this.clientTopicListService.setValueClass(ClientTopic.class);
	}

	@Autowired
	private void setConsumerDataListService(
			IRedisHashService<ConsumerClientData> consumerDataListService) {
		this.consumerDataListService = consumerDataListService;
		this.consumerDataListService.setValueClass(ConsumerClientData.class);
	}

	@Autowired
	private void setProcedureDataService(
			IRedisHashService<ProcedureClientData> procedureDataService) {
		this.procedureDataService = procedureDataService;
		this.procedureDataService.setValueClass(ProcedureClientData.class);
	}

	@Autowired
	private void setRemoteSessionListService(
			IRedisHashService<MqttSession> remoteSessionListService) {
		this.remoteSessionListService = remoteSessionListService;
		this.remoteSessionListService.setValueClass(MqttSession.class);
	}

	@Autowired
	private void setUniqueIdListService(
			IRedisHashService<UniqueIdInteger> uniqueIdListService) {
		this.uniqueIdListService = uniqueIdListService;
		this.uniqueIdListService.setValueClass(UniqueIdInteger.class);
	}

	@Override
	public void init(MqttServer mqttServer) {
		super.init(mqttServer);

		CacheList<UniqueIdInteger> globalUniquedIdList = new CacheListRedis<UniqueIdInteger>(
				"mqtt:s:client:gu:", uniqueIdListService);
		CacheList<TopicData> topicList = new CacheListRedis<TopicData>(
				"mqtt:s:topic:", topicListService);
		CacheList<ClientTopic> clientTopicList = new CacheListRedis<ClientTopic>(
				"mqtt:s:client:topic:", clientTopicListService);
		CacheList<ConsumerClientData> consumerList = new CacheListRedis<ConsumerClientData>(
				"mqtt:s:client:consumer:", consumerDataListService);
		CacheList<ProcedureClientData> procedureList = new CacheListRedis<ProcedureClientData>(
				"mqtt:s:client:procedure:", procedureDataService);

		CacheList<MqttSession> remoteSessionList = new CacheListRedis<MqttSession>(
				"mqtt:s:client:session:", remoteSessionListService);

		CustomConfig cfg = mqttServer.initMqtt();
		cfg.setGlobalUniqueIdCacheList(globalUniquedIdList);
		cfg.setClientTopicList(clientTopicList);
		cfg.setTopicList(topicList);
		cfg.setConsumerCacheList(consumerList);
		cfg.setProcedureCacheList(procedureList);
		cfg.setRemoteSessionCache(remoteSessionList);
	}
}
