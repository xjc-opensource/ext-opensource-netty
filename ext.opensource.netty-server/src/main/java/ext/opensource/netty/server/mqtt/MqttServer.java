package ext.opensource.netty.server.mqtt;

import ext.opensource.netty.server.core.BaseServer;
import ext.opensource.netty.server.mqtt.api.CustomConfig;
import ext.opensource.netty.server.mqtt.api.InternalRecvice;
import ext.opensource.netty.server.mqtt.common.InternalMessage;
import ext.opensource.netty.server.mqtt.protocol.ProtocolProcess;
import ext.opensource.netty.server.mqtt.protocol.ProtocolProcessConfig;
import ext.opensource.netty.server.mqtt.protocol.ProtocolUtil;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttServer extends BaseServer {
	private ProtocolProcessConfig cfg;
	private ProtocolProcess protocolProcess;
	
	public MqttServer() {
		checkHeartbeat = true;
		readerIdleTimeSeconds = 0;
		writerIdleTimeSeconds = 0; 
		allIdleTimeSeconds = 60;
		
		protocolProcess = new ProtocolProcess();
		cfg = new ProtocolProcessConfig(protocolProcess);
		protocolProcess.init(cfg);
	}
	
	public InternalRecvice internalRecvice() {
		return this.protocolProcess;
	}
	
	public CustomConfig initMqtt() {
		return cfg;
	}
	
	@Override
	public void broadcastMessageString(String msg) {
		this.broadcastMessage(ProtocolUtil.publishMessage("/broadcast", msg.getBytes()));
	}
 
	@Override
	protected void initSocketChannel(SocketChannel ch) {
		super.initSocketChannel(ch);
		ch.pipeline().addLast("mqttDecoder", new MqttDecoder());
		ch.pipeline().addLast("mqttEncoder", MqttEncoder.INSTANCE);
		ch.pipeline().addLast("mqttHander", new MqttServerHandler(protocolProcess));
	}
	
	public void testInternalMessage() {	
		InternalMessage msg =
				InternalMessage.builder().topicName("testInternalMessage").build(); 
		if (cfg.consumerProcess.getInternalSend() != null) {
			cfg.consumerProcess.getInternalSend().internalSend(msg);
		}
	}
}
