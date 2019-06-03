
package ext.opensource.netty.server.example.mqtt.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Configuration
@EnableKafka
public class KafkaProducerConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String servers;
	@Value("${spring.kafka.producer.acks}")
	private String acks;
	@Value("${spring.kafka.producer.retries}")
	private String retries;
	@Value("${spring.kafka.producer.batch-size}")
	private String batchSize;
	@Value("${spring.kafka.producer.linger.ms}")
	private String lingerMs;
	@Value("${spring.kafka.producer.buffer-memory}")
	private String bufferMemory;

	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>(16);
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ProducerConfig.ACKS_CONFIG, acks);
		props.put(ProducerConfig.RETRIES_CONFIG, retries);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
		props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		return props;
	}

	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<String, String>(producerFactory());
	}
	

}