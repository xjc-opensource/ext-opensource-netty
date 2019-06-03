package ext.opensource.netty.client.example.mqtt;

import org.infrastructure.redis.IRedisHashService;
import org.springframework.beans.factory.annotation.Autowired;

import ext.opensource.netty.client.example.redis.CacheListRedis;
import ext.opensource.netty.client.mqtt.MqttClient;
import ext.opensource.netty.client.mqtt.common.DefautMqttConsumerListener;
import ext.opensource.netty.client.mqtt.common.MessageData;
import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class CustomMqttClientRedis extends CustomMqttClient {
	private IRedisHashService<MessageData> redisHashService;
	@Autowired
	private void setRedisHashService(IRedisHashService<MessageData> redisHashService) {
		this.redisHashService = redisHashService;
		this.redisHashService.setValueClass(MessageData.class);
	}
	
	private IRedisHashService<UniqueIdInteger> uniqueIdListService;
	@Autowired
	private void setUniqueIdListService(IRedisHashService<UniqueIdInteger> uniqueIdListService) {
		this.uniqueIdListService = uniqueIdListService;
		this.uniqueIdListService.setValueClass(UniqueIdInteger.class);
	}
	
	@Override
	public void init(MqttClient nettyClient) {
		super.init(nettyClient);
		
		String clientId = nettyClient.mqttOptions().getClientIdentifier();
		
		CacheList<MessageData> consumerCache = new CacheListRedis<MessageData>(
				"mqttclient:consumer:" + clientId, redisHashService);
		CacheList<MessageData> procedureCache = new CacheListRedis<MessageData>(
				"mqttclient:procedure:" + clientId, redisHashService);
		
		CacheList<UniqueIdInteger> idCache = new CacheListRedis<UniqueIdInteger>(
				"mqttclient:gid:" + clientId, uniqueIdListService);
		
		nettyClient.consumer().setGlobalUniqueIdCache(idCache);
		nettyClient.consumer().setCacheList(consumerCache);
		nettyClient.consumer().setConsumerListener(new DefautMqttConsumerListener());

		nettyClient.producer().setGlobalUniqueIdCache(idCache);
		nettyClient.producer().setCacheList(procedureCache);	
	}
}
