
package ext.opensource.netty.server.example.mqtt;

import ext.opensource.netty.server.mqtt.MqttServer;

/**
 * @author ben
 * @Title: CustomInit.java
 * @Description:
 **/

public interface MqttCustomInit {
	/**
	 * init
	 * @param mqttServer
	 */
	public void init(MqttServer mqttServer);
}
