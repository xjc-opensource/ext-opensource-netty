package ext.opensource.netty.client.mqtt.api.impl;

import ext.opensource.netty.client.core.BaseClient;
import ext.opensource.netty.client.mqtt.api.ClientProcess;
import ext.opensource.netty.client.mqtt.api.MqttProducer;
import ext.opensource.netty.client.mqtt.api.MqttProducerProcess;
import ext.opensource.netty.client.mqtt.common.MessageData;
import ext.opensource.netty.client.mqtt.common.MessageStatus;
import ext.opensource.netty.client.mqtt.protocol.ClientProtocolUtil;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.CacheListLocalMemory;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public class MqttProducerProcessImpl extends ClientProcess implements MqttProducerProcess, MqttProducer {
	@Setter
	@NonNull
	private CacheList<MessageData> msgListCache;

	public MqttProducerProcessImpl(BaseClient client) {
		this(client, new CacheListLocalMemory<MessageData>());
	}
	
	public MqttProducerProcessImpl(BaseClient client, CacheList<MessageData> msgListCache) {
		super(client);
		this.msgListCache = msgListCache;
	}

	private MessageData buildMqttMessage(String topic, String message, int qosValue, boolean isReatain, boolean dup) {
		int msgId = 0;
		if (qosValue == MqttQoS.AT_LEAST_ONCE.value()) {
			msgId = messageId().getNextMessageId(getClientId());
		} else if (qosValue == MqttQoS.EXACTLY_ONCE.value()) {
			msgId = messageId().getNextMessageId(getClientId());
		}

		return MessageData.builder().messageId(msgId).topic(topic).dup(dup).retained(isReatain).qos(qosValue)
				.status(MessageStatus.PUB)
				.timestamp(System.currentTimeMillis()).payload(encoded(message))
				.build();
	}

	private void sendPubishMessage(MessageData sendMqttMessage) {
		if (sendMqttMessage == null) {
			return;
		}
		if (!channel().isActive()) {
			NettyLog.debug("channel is close");
			return;
		}
		saveMessage(sendMqttMessage);

		NettyLog.debug("sendPubishMessage: {}", sendMqttMessage.toString());
		channel().writeAndFlush(ClientProtocolUtil.publishMessage(sendMqttMessage));
	}

	@Override
	public void saveMessage(MessageData sendMqttMessage) {
		if (msgListCache == null) {
			return;
		}
		if ((sendMqttMessage != null) && (sendMqttMessage.getMessageId() > 0)) {
			NettyLog.debug("saveMessage: {}", sendMqttMessage.getMessageId());
			msgListCache.put(String.valueOf(sendMqttMessage.getMessageId()), sendMqttMessage);
		}
	}

	@Override
	public void delMessage(int messageId) {
		if (msgListCache == null) {
			return;
		}
		
		if (messageId > 0) {
			NettyLog.debug("delMessage: {}", messageId);
			msgListCache.remove(String.valueOf(messageId));
		}
	}
	
	public void changeMessageStatus(int messageId, MessageStatus status) {
		if (msgListCache == null) {
			return;
		}
		
		MessageData msgObj = msgListCache.get(String.valueOf(messageId));
		if (msgObj != null) {
			msgObj.setStatus(status);
			msgListCache.put(String.valueOf(messageId), msgObj);
		}
	}

	@Override
	public void sendPubishMessage(String topic, String message, int qosValue, boolean isRetain) {
		sendPubishMessage(buildMqttMessage(topic, message, qosValue, isRetain, false));
	}

	@Override
	public void sendPubRel(int messageId) {
		NettyLog.debug("send Pub-Rel: {}", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubRelMessage(messageId, false));
	}

	@Override
	public void processPubRec(int messageId) {
		NettyLog.debug("process Pub-Rec: {}", messageId);
		changeMessageStatus(messageId, MessageStatus.PUBREC);
	}

	@Override
	public void processPubAck(int messageId) {
		NettyLog.debug("process Pub-Ack: {} ; list size: {}", messageId, msgListCache.size());
	}

	@Override
	public void processPubComp(int messageId) {
		NettyLog.debug("process Pub-Comp: {}", messageId);
	}

	@Override
	public void setCacheList(CacheList<MessageData> msgList) {
		if (msgList != null) {
			this.msgListCache = msgList;
		}
	}
}
