package ext.opensource.netty.server.example.mqtt.redis.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Configuration
public class RedisSubListenerConfig {
	public static final String MQTT_TOPIC_NAME="mqtt-index";
	@Bean
	@Conditional(RedisConditionalCommunicate.class)
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(MQTT_TOPIC_NAME));
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(RedisSubribleLister redisSubribleLister) {
		return new MessageListenerAdapter(redisSubribleLister, "receiveMessage");
	}
	
	@Bean
	CountDownLatch latch() {
		return new CountDownLatch(1);
	}

	@Bean("name=sendRedisTemplate")
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

}