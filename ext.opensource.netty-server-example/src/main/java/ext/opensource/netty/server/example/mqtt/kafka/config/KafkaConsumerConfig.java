
package ext.opensource.netty.server.example.mqtt.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import ext.opensource.netty.common.utils.StringsUtil;
import java.util.HashMap;
import java.util.Map;
 
/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.consumer.group-id:}")
    private String groupId;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String enableAutoCommit;
    
    @Value("${spring.kafka.consumer.auto-offset-reset:latest}")
    private String autoOffsetReset;
    
    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval;
 
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }
 
    private ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }
 
 
    private Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>(16);
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        String sId = groupId;
        if (groupId == null || (groupId.trim().length() <= 0)) {
        	sId = StringsUtil.getUuid();
        }
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, sId);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,autoCommitInterval);
        
        ///latest,none,earliest
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return propsMap;
    }
    
}
