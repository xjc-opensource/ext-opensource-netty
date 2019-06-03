package ext.opensource.netty.server.example.mqtt;

import ext.opensource.netty.server.mqtt.MqttServer;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class BaseMqttCustom implements MqttCustomInit{
	protected MqttServer mqttServer;

	@Override
	public void init(MqttServer mqttServer) {
		this.mqttServer = mqttServer;
	}
}
