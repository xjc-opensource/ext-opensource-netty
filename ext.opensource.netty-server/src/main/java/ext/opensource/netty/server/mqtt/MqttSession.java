
package ext.opensource.netty.server.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import java.io.Serializable;

import ext.opensource.netty.common.BaseSocketSession;
import ext.opensource.netty.common.exception.MethodNotSupportException;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttSession extends BaseSocketSession implements Serializable {
	private static final long serialVersionUID = 1L;
	private String clientId;
	private boolean cleanSession;
	private transient MqttPublishMessage willMessage;

	public MqttSession(String clientId, Channel channel, boolean cleanSession, MqttPublishMessage willMessage) {
		super(channel);
		
		this.clientId = clientId;
		this.cleanSession = cleanSession;
		this.willMessage = willMessage;
	}

	public String getClientId() {
		return clientId;
	}

	public MqttSession setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public MqttSession setCleanSession(boolean cleanSession) {
		this.cleanSession = cleanSession;
		return this;
	}

	public MqttPublishMessage getWillMessage() {
		return willMessage;
	}

	public MqttSession setWillMessage(MqttPublishMessage willMessage) {
		this.willMessage = willMessage;
		return this;
	}

	@Override
	public ChannelFuture sendTextMessage(String message) {
		throw new MethodNotSupportException(); 
	}

	@Override
	public ChannelFuture sendBinaryMessage(ByteBuf byteBuf) {
		throw new MethodNotSupportException(); 
	}
}
