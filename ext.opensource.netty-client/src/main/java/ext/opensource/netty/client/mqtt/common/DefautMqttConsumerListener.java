package ext.opensource.netty.client.mqtt.common;

import ext.opensource.netty.client.mqtt.api.MqttConsumerListener;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public class DefautMqttConsumerListener implements MqttConsumerListener {

	@Override
	public void receiveMessage(int msgId, String topic, String msg) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void receiveMessageByAny(int msgId, String topic, String msg) {
		// TODO Auto-generated method stub
	}
}
