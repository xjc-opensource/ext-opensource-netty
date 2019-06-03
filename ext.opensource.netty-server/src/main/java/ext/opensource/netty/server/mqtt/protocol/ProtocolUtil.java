package ext.opensource.netty.server.mqtt.protocol;

import ext.opensource.netty.common.MqttProtocolUtil;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProtocolUtil extends MqttProtocolUtil {
	public static MqttPublishMessage publishMessage(String topic, byte[] payload) {
		return publishMessage(topic, payload, 0, 10, false);
	}
}
