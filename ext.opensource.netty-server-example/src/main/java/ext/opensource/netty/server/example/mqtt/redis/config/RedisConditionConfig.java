
package ext.opensource.netty.server.example.mqtt.redis.config;

import org.infrastructure.redis.config.RedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ext.opensource.netty.server.example.mqtt.redis.CustomRedisCommunicate;
import ext.opensource.netty.server.example.mqtt.redis.CustomRedisCache;

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
	CustomRedisCache customRedisCache() {
		return new CustomRedisCache();
	}
	
	@Bean
	@Conditional(RedisConditionalCommunicate.class)
	CustomRedisCommunicate customCommunicateRedis() {
		return new CustomRedisCommunicate();
	}
}
