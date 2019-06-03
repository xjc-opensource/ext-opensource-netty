package ext.opensource.netty.client.mqtt.api.impl;

import java.util.ArrayList;
import java.util.List;

import ext.opensource.netty.client.core.BaseClient;
import ext.opensource.netty.client.mqtt.api.ClientProcess;
import ext.opensource.netty.client.mqtt.api.MqttConsumer;
import ext.opensource.netty.client.mqtt.api.MqttConsumerListener;
import ext.opensource.netty.client.mqtt.api.MqttConsumerProcess;
import ext.opensource.netty.client.mqtt.common.MessageData;
import ext.opensource.netty.client.mqtt.common.MessageStatus;
import ext.opensource.netty.client.mqtt.common.SubscribeMessage;
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

public class MqttConsumerProcessImpl extends ClientProcess implements MqttConsumerProcess, MqttConsumer {
	private MqttConsumerListener listener;
	@Setter
	@NonNull
	private CacheList<MessageData> msgListCache;
	
	public MqttConsumerProcessImpl(BaseClient client) {
		this(client, new CacheListLocalMemory<MessageData>());
	}
	
	public MqttConsumerProcessImpl(BaseClient client, CacheList<MessageData> msgListCache) {
		super(client);
		this.msgListCache = msgListCache;
	}

	@Override
	public void subscribe(String topic, int qosValue) {
		SubscribeMessage msgObj = SubscribeMessage.builder().topic(topic).qos(qosValue).build();
		subscribe(msgObj);
	}

	public void subscribe(SubscribeMessage info) {
		NettyLog.debug("subscribe： {} ", info);
		subscribe(new SubscribeMessage[] { info });
	}

	private void subscribe(SubscribeMessage... info) {
		if (info != null) {
			int messageId = messageId().getNextMessageId(this.getClientId());
			if (!channel().isActive()) {
				NettyLog.debug("channel is close");
				return;
			}
			
			channel().writeAndFlush(ClientProtocolUtil.subscribeMessage(messageId, info));
		}
	}

	public void unSubscribe(String topic) {
		List<String> topics = new ArrayList<String>();
		topics.add(topic);
		unSubscribe(topics);
	}

	public void unSubscribe(List<String> topics) {
		if (topics != null) {
			int messageId = messageId().getNextMessageId(this.getClientId());
			channel().writeAndFlush(ClientProtocolUtil.unSubscribeMessage(topics, messageId));
		}
	}

	@Override
	public void saveMesage(MessageData recviceMessage) {
		//if (msgListCache == null) {return;}
		if ((recviceMessage != null) && (recviceMessage.getMessageId() > 0)) {
			NettyLog.debug("saveMessage: {}", recviceMessage.getMessageId());
			msgListCache.put(String.valueOf(recviceMessage.getMessageId()), recviceMessage);
		}
	}

	@Override
	public void delMesage(int messageId) {
		if (msgListCache == null) {return;}
		if (messageId > 0) {
			NettyLog.debug("delMesage: {}", messageId);
			msgListCache.remove(String.valueOf(messageId));
		}
	}

	public MessageData changeMessageStatus(int messageId, MessageStatus status) {
		if (msgListCache == null) {return null;}
		NettyLog.debug("changeMessageStatus: {}", status.name());

		MessageData msgObj = msgListCache.get(String.valueOf(messageId));
		if (msgObj != null) {
			msgObj.setStatus(status);
			msgListCache.put(String.valueOf(messageId), msgObj);
		}
		return msgObj;
	}

	@Override
	public void processPubRel(int messageId) {
		MessageData msgObj = changeMessageStatus(messageId, MessageStatus.PUBREL);

		if ((msgObj != null) && (listener != null)) {
			if (msgObj.getQos() == MqttQoS.EXACTLY_ONCE.value()) {
				listener.receiveMessage(msgObj.getMessageId(), msgObj.getTopic(), decode(msgObj.getPayload()));
			}
		}
		NettyLog.debug("process Pub-rel: messageId - {} ", messageId);

	}

	@Override
	public void processSubAck(int messageId) {
		NettyLog.debug("process Sub-ack: messageId - {} ", messageId);
	}

	@Override
	public void processUnSubBack(int messageId) {
		NettyLog.debug("process Un-sub-back: messageId - {} ", messageId);
	}

	@Override
	public void processPublish(MessageData msg) {
		NettyLog.debug("process Publish: {} ", msg);

		if (listener != null) {
			listener.receiveMessageByAny(msg.getMessageId(), msg.getTopic(), decode(msg.getPayload()));

			if (msg.getQos() == MqttQoS.AT_MOST_ONCE.value() || msg.getQos() == MqttQoS.EXACTLY_ONCE.value()) {
				listener.receiveMessage(msg.getMessageId(), msg.getTopic(), decode(msg.getPayload()));
			}
		}
	}

	@Override
	public void sendPubRecMessage(int messageId) {
		NettyLog.debug("send Pub-rec: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubRecMessage(messageId));
	}

	@Override
	public void sendPubAckMessage(int messageId) {
		NettyLog.debug("send Pub-ack: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubAckMessage(messageId));
	}

	@Override
	public void sendPubCompMessage(int messageId) {
		NettyLog.debug("send Pub-comp: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubCompMessage(messageId));
	}

	@Override
	public void setConsumerListener(MqttConsumerListener listener) {
		this.listener = listener;
	}

	@Override
	public void setCacheList(CacheList<MessageData> msgList) {
		if (msgList != null) {
			this.msgListCache = msgList;
		}
	}
}
