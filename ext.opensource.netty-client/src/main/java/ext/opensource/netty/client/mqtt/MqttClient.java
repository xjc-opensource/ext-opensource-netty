package ext.opensource.netty.client.mqtt;

import ext.opensource.netty.client.core.BaseClient;
import ext.opensource.netty.client.mqtt.api.MqttConsumer;
import ext.opensource.netty.client.mqtt.api.MqttConsumerProcess;
import ext.opensource.netty.client.mqtt.api.MqttProducer;
import ext.opensource.netty.client.mqtt.api.MqttProducerProcess;
import ext.opensource.netty.client.mqtt.api.impl.MqttConsumerProcessImpl;
import ext.opensource.netty.client.mqtt.api.impl.MqttProducerProcessImpl;
import ext.opensource.netty.client.mqtt.common.MqttConnectOptions;
import ext.opensource.netty.client.mqtt.protocol.ClientProtocolProcess;
import ext.opensource.netty.client.mqtt.protocol.ClientProtocolUtil;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.exception.MethodNotSupportException;
import ext.opensource.netty.common.utils.StringsUtil;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttClient extends BaseClient {
	private MqttConnectOptions info = new MqttConnectOptions();
	private MqttProducerProcess producerProcess;
	private MqttConsumerProcess consumerProcess;
	private MqttConsumer consumer;
	private MqttProducer producer;

	public MqttClient() {
		this.setSyncConnect(true);

		info.setClientIdentifier(StringsUtil.getUuid());
		MqttProducerProcessImpl producerProcessObj = new MqttProducerProcessImpl(this);
		MqttConsumerProcessImpl consumerProcessObj = new MqttConsumerProcessImpl(this);

		producerProcess = producerProcessObj;
		consumerProcess = consumerProcessObj;
		
		consumer = consumerProcess;
		producer = producerProcess;

	}
	
	public MqttConnectOptions mqttOptions() {
		return info;
	}

	public MqttConsumer consumer() {
		return consumer;
	}

	public MqttProducer producer() {
		return producer;
	}
	
	@Override
	public String getClientId() {
		return info.getClientIdentifier();
	}

	@Override
	protected void initConnect() {
		super.initConnect();
	}

	@Override
	public void sendMessage(String msg) {
		throw new MethodNotSupportException();
	}

	@Override
	protected void initSocketChannel(SocketChannel ch) {
		ClientProtocolProcess proObj = new ClientProtocolProcess(this, consumerProcess, producerProcess);
		super.initSocketChannel(ch);

		ch.pipeline().addLast("decoder", new MqttDecoder());
		ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
		ch.pipeline().addLast("mqttHander", new MqttClientHandler(proObj));
	}

	@Override
	protected boolean loginInit() {
		NettyLog.debug("loginInit: " + info.toString());
		channel.writeAndFlush(ClientProtocolUtil.connectMessage(info));
		return true;
	}

	@Override
	public void disConnect() {
		if (channel != null) {
			NettyLog.debug("disConnect: ");
			channel.writeAndFlush(ClientProtocolUtil.disConnectMessage());
		}
	}
}
