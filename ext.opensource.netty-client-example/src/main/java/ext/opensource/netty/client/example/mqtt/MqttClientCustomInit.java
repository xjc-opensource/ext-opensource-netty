
package ext.opensource.netty.client.example.mqtt;

import ext.opensource.netty.client.mqtt.MqttClient;

/**
 * @author ben
 * @Title: CustomInit.java
 * @Description:
 **/

public interface MqttClientCustomInit {
	/**
	 * init
	 * @param nettyClient
	 */
	public void init(MqttClient nettyClient);
}
