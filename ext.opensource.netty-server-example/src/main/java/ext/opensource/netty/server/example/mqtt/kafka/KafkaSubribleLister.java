
package ext.opensource.netty.server.example.mqtt.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.server.example.mqtt.MqttServerRecvice;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class KafkaSubribleLister {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Setter
	private MqttServerRecvice serverRecvice;

	public void sendPublishMessage(String message) {
		NettyLog.info("internalSend: " + message);
		kafkaTemplate.send("mqtt-internal", message);
	}

	@KafkaListener(topics = {"mqtt-internal"})
	public void consumer(ConsumerRecord<String, String> consumerRecord) {
		String kafkaMessage = consumerRecord.value();
		NettyLog.info("internalRecvice: " + kafkaMessage);
		
		if (kafkaMessage != null) {			
			if (serverRecvice != null) {
				serverRecvice.processServerRecviceMesage(kafkaMessage);
			}
		}
	}

}
