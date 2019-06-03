
package ext.opensource.netty.server.example.mqtt.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import ext.opensource.netty.server.example.mqtt.kafka.CustomKafkaCommunicate;
import ext.opensource.netty.server.example.mqtt.kafka.KafkaSubribleLister;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Configuration
public class KafkaConditionConfig {
	@Bean
	@Conditional(KafkaConditionalCommunicate.class)
	public KafkaSubribleLister kafkaSubribleLister(){
		return new KafkaSubribleLister();
	}
	
	@Bean
	@Conditional(KafkaConditionalCommunicate.class)
	public CustomKafkaCommunicate customKafkaCommunicate() {
		return new CustomKafkaCommunicate();
	}
}
