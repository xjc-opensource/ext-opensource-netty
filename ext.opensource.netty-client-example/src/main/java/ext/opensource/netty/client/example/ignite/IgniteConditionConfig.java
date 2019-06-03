
package ext.opensource.netty.client.example.ignite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import ext.opensource.netty.client.example.mqtt.CustomMqttClientIgnite;

/**
 * @author ben
 * @Title: xx.java
 * @Description:
 **/
@Configuration
public class IgniteConditionConfig {
	@Bean
	@Conditional(IgniteConditionalCache.class)
	CustomMqttClientIgnite customMqttClientIgnite() {
		return new CustomMqttClientIgnite();
	}
}
