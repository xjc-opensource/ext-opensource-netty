package ext.opensource.netty.server.mqtt.protocol.process;

import java.util.List;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.NettyUtil;
import ext.opensource.netty.server.mqtt.common.ConsumerMessage;
import ext.opensource.netty.server.mqtt.protocol.ProtocolUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class SendMessageProcess {

	/**
	 *  B - S (Qos1, Qos2)
	 * @param channel
	 * @param info
	 */
	public void sendPublishRetryMessage(Channel channel, ConsumerMessage info) {
		NettyLog.debug("sendPublishRetryMessage: {}", info);
		if (info != null) {
			this.sendPublishMessage(channel, info.getTopic(), info.isDup(), info.getMqttQoS(), info.isRetain(),
					info.getMessageId(), info.getMessageBytes());
		}
	}

	/**
	 *  B - S (Qos0 Qos1 Qos2)
	 * @param channel
	 * @param topicName
	 * @param isDup
	 * @param iQoS
	 * @param isRetain
	 * @param messageId
	 * @param bytes
	 */
	public void sendPublishMessage(Channel channel, String topicName, boolean isDup, int iQoS, boolean isRetain,
			int messageId, byte[] bytes) {
		NettyLog.debug("sendPublishMessage: msgId- {}", messageId);
		channel.writeAndFlush(ProtocolUtil.publishMessage(topicName, isDup, iQoS, isRetain, messageId, bytes));
	}

	/**
	 * sendPublishMessage obj
	 * @param channel
	 * @param publishMessage
	 */
	public void sendPublishMessage(Channel channel, MqttPublishMessage publishMessage) {
		NettyLog.debug("sendPublishMessage");
		channel.writeAndFlush(publishMessage);
	}


	/**
	 *  B - P (Qos2)
	 * @param channel
	 * @param msgId
	 */
	public void sendPubCompMessage(Channel channel, int msgId) {
		NettyLog.debug("sendPubCompMessage");
		channel.writeAndFlush(ProtocolUtil.pubCompMessage(msgId));
	}


	/**
	 *  B - P (Qos1)
	 * @param channel
	 * @param messageId
	 */
	public void sendPubAckMessage(Channel channel, int messageId) {
		NettyLog.debug("sendPubAckMessage");
		channel.writeAndFlush(ProtocolUtil.pubAckMessage(messageId));
	}


	/**
	 *  B - P (Qos2)
	 * @param channel
	 * @param messageId
	 */
	public void sendPubRecMessage(Channel channel, int messageId) {
		NettyLog.debug("sendPubRecMessage");
		channel.writeAndFlush(ProtocolUtil.pubRecMessage(messageId));
	}

	/**
	 *  B - P, B - S
	 * @param channel
	 * @param code
	 * @param bSessionPresent
	 */

	public void sendBackConnect(Channel channel, MqttConnectReturnCode code, boolean bSessionPresent) {
		NettyLog.debug("sendBackConnect");
		channel.writeAndFlush(ProtocolUtil.connAckMessage(code, bSessionPresent));
	}
	
	/**
	 *  B - S, B-P
	 * @param channel
	 */
	public void sendPingRespMessage(Channel channel) {
		NettyLog.debug("sendPingRespMessage - clientId: {}", NettyUtil.getClientId(channel));
		channel.writeAndFlush(ProtocolUtil.pingRespMessage());
	}

	/**
	 *  B - S
	 * @param channel
	 * @param messageId
	 * @param mqttQoSList
	 */
	public void sendSubAckMessage(Channel channel, int messageId, List<Integer> mqttQoSList) {
		NettyLog.debug("sendSubAckMessage - clientId: {}, msgID: {}", NettyUtil.getClientId(channel), messageId);
		channel.writeAndFlush(ProtocolUtil.subAckMessage(messageId, mqttQoSList));
	}
	
	/**
	 *  B - S
	 * @param channel
	 * @param messageId
	 * @param bDup
	 */
	public void sendPubRelMessage(Channel channel, int messageId, boolean bDup) {
		NettyLog.debug("sendPubRelMessage - clientId: {}, msgID: {}", NettyUtil.getClientId(channel), messageId);
		channel.writeAndFlush(ProtocolUtil.pubRelMessage(messageId, bDup));
	}
	
	/**
	 *  B - S
	 * @param channel
	 * @param messageId
	 */
	public void sendUnSubAckMessage(Channel channel, int messageId) {
		NettyLog.debug("sendUnSubAckMessage - clientId: {}, msgID: {}", NettyUtil.getClientId(channel), messageId);
		channel.writeAndFlush(ProtocolUtil.unsubAckMessage(messageId));
	}
}
