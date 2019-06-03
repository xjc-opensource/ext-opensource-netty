package ext.opensource.netty.client.mqtt.api;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttConsumerListener {

	/**
	 * 接收到的消息(确认过的)
	 * @param msgId
	 * @param topic
	 * @param msg
	 */
	void receiveMessage(int msgId, String topic, String msg);
	

	/**
	 * 接收到的消息(收到就转发)
	 * @param msgId
	 * @param topic
	 * @param msg
	 */
	void receiveMessageByAny(int msgId, String topic, String msg);
}
