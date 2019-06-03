package ext.opensource.netty.client.example.mqtt;

import javax.annotation.Resource;

import org.apache.ignite.Ignite;

import ext.opensource.netty.client.example.ignite.CacheListIgnite;
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

public class CustomMqttClientIgnite extends CustomMqttClient {
	@Resource
	private Ignite ignite;

	@Override
	public void init(MqttClient nettyClient) {
		super.init(nettyClient);
		
		String clientId = nettyClient.mqttOptions().getClientIdentifier();
		
		CacheList<MessageData> consumerCache = new CacheListIgnite<MessageData>(
				"mqttclient-consumer-" + clientId, ignite);
		CacheList<MessageData> procedureCache = new CacheListIgnite<MessageData>(
				"mqttclient-procedure-" + clientId, ignite);
		
		CacheList<UniqueIdInteger> idCache = new CacheListIgnite<UniqueIdInteger>(
				"mqttclient-gid-" + clientId, ignite);

		nettyClient.consumer().setGlobalUniqueIdCache(idCache);
		nettyClient.consumer().setCacheList(consumerCache);
		nettyClient.consumer().setConsumerListener(new DefautMqttConsumerListener());
		nettyClient.producer().setGlobalUniqueIdCache(idCache);
		nettyClient.producer().setCacheList(procedureCache);
	}
}
