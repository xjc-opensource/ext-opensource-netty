
package ext.opensource.netty.client.example.redis;

import org.infrastructure.redis.config.RedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ext.opensource.netty.client.example.mqtt.CustomMqttClientRedis;

/**
 * @author ben
 * @Title: xx.java
 * @Description:
 **/
@Configuration
@Import({ RedisConfig.class })
public class RedisConditionConfig {
	@Bean
	@Conditional(RedisConditionalCache.class)
	CustomMqttClientRedis customMqttClientRedis() {
		return new CustomMqttClientRedis();
	}
}
