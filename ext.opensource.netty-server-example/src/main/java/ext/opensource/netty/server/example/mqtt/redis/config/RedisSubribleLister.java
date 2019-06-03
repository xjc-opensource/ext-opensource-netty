package ext.opensource.netty.server.example.mqtt.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.server.example.mqtt.MqttServerRecvice;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class RedisSubribleLister {
	@Setter
	private MqttServerRecvice serverRecvice;
	@Autowired
	private StringRedisTemplate sendRedisTemplate;

	public void sendPublishMessage(String message) {
		NettyLog.info("internalSend: " + message);
		sendRedisTemplate.convertAndSend(RedisSubListenerConfig.MQTT_TOPIC_NAME, message);
	}

	public void receiveMessage(String message) {
		NettyLog.info("internalRecvice: " + message);
		
		if (serverRecvice != null) {
			serverRecvice.processServerRecviceMesage(message);
		}
	}
}
