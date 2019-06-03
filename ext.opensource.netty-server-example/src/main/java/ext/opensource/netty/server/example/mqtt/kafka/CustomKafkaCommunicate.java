package ext.opensource.netty.server.example.mqtt.kafka;

import org.springframework.beans.factory.annotation.Autowired;

import ext.opensource.netty.server.example.mqtt.MqttCustomCommunicate;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/ 

public class CustomKafkaCommunicate extends MqttCustomCommunicate {
	private KafkaSubribleLister kafkaSubribleLister;
	@Autowired
	public void setKafkaSubribleLister(KafkaSubribleLister kafkaSubribleLister) {
		this.kafkaSubribleLister = kafkaSubribleLister;
		this.kafkaSubribleLister.setServerRecvice(this);
	}

	@Override
	protected void processInternalSend(String msg) {
		kafkaSubribleLister.sendPublishMessage(msg);
	}
}
