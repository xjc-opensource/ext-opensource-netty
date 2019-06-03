package ext.opensource.netty.server.example.mqtt.redis;

import org.springframework.beans.factory.annotation.Autowired;

import ext.opensource.netty.server.example.mqtt.MqttCustomCommunicate;
import ext.opensource.netty.server.example.mqtt.redis.config.RedisSubribleLister;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class CustomRedisCommunicate extends MqttCustomCommunicate {
	private RedisSubribleLister redisSubribleLister;

	@Autowired
	public void setInternalRedisLister(
			RedisSubribleLister redisSubribleLister) {
		this.redisSubribleLister = redisSubribleLister;
		this.redisSubribleLister.setServerRecvice(this);
	}

	@Override
	protected void processInternalSend(String msg) {
		redisSubribleLister.sendPublishMessage(msg);
	}

	/*
	 * @Scheduled(fixedRate = 3000) public void test() { InternalMessage msg =
	 * InternalMessage.builder().topicName("aaa").build(); internalSend(msg); }
	 */
}
