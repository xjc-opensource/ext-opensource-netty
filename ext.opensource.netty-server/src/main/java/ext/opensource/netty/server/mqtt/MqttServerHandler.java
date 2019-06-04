package ext.opensource.netty.server.mqtt;

import java.io.IOException;

import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.NettyUtil;
import ext.opensource.netty.server.mqtt.protocol.ProtocolProcess;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttServerHandler extends SimpleChannelInboundHandler<MqttMessage> {

	private ProtocolProcess protocolProcess;

	public MqttServerHandler(ProtocolProcess protocolProcess) {
		this.protocolProcess = protocolProcess;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
		NettyLog.debug("read: " + msg.fixedHeader().messageType());
		
		if (msg.fixedHeader().messageType() == MqttMessageType.CONNECT) {
			protocolProcess.processConnect(ctx.channel(), (MqttConnectMessage) msg);
		} else {
			if (!NettyUtil.isLogin(ctx.channel())) {
				NettyLog.info("not login");
				return ;
			}
		}

		switch (msg.fixedHeader().messageType()) {
		case CONNECT:
			break;
		case CONNACK:
			break;
		case PUBLISH:
			protocolProcess.processPublish(ctx.channel(), (MqttPublishMessage) msg);
			break;
		case PUBACK:
			protocolProcess.processPubAck(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case PUBREC:
			protocolProcess.processPubRec(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case PUBREL:
			protocolProcess.processPubRel(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case PUBCOMP:
			protocolProcess.processPubComp(ctx.channel(), (MqttMessageIdVariableHeader) msg.variableHeader());
			break;
		case SUBSCRIBE:
			protocolProcess.processSubscribe(ctx.channel(), (MqttSubscribeMessage) msg); 
			break;
		case SUBACK:
			break;
		case UNSUBSCRIBE:
			protocolProcess.processUnSubscribe(ctx.channel(), (MqttUnsubscribeMessage) msg);
			break;
		case UNSUBACK:
			break;
		case PINGREQ:
			protocolProcess.processPingReq(ctx.channel(), msg);
			break;
		case PINGRESP:
			break;
		case DISCONNECT:
			protocolProcess.processDisConnect(ctx.channel(), msg);
			break;
		default:
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) {
			// 远程主机强迫关闭了一个现有的连接的异常
			ctx.close();
		} else {
			super.exceptionCaught(ctx, cause);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			if (idleStateEvent.state() == IdleState.ALL_IDLE) {
				this.protocolProcess.processWillMessage(ctx.channel());
				ctx.close();
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}