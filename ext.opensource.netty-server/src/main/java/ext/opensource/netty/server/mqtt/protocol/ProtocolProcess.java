package ext.opensource.netty.server.mqtt.protocol;

import java.util.List;
import ext.opensource.netty.common.NettyConstant;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.NettyUtil;
import ext.opensource.netty.common.utils.ByteBufUtil;
import ext.opensource.netty.server.mqtt.MqttSession;
import ext.opensource.netty.server.mqtt.api.MqttAuth;
import ext.opensource.netty.server.mqtt.api.InternalRecvice;
import ext.opensource.netty.server.mqtt.api.MqttSessionService;
import ext.opensource.netty.server.mqtt.api.PubishMessageLister;
import ext.opensource.netty.server.mqtt.api.impl.AuthServiceImpl;
import ext.opensource.netty.server.mqtt.common.BorkerMessage;
import ext.opensource.netty.server.mqtt.common.InternalMessage;
import ext.opensource.netty.server.mqtt.common.ProcedureMessage;
import ext.opensource.netty.server.mqtt.common.RetainMessage;
import ext.opensource.netty.server.mqtt.common.SubscribeTopicInfo;
import ext.opensource.netty.server.mqtt.protocol.process.ConsumerProcess;
import ext.opensource.netty.server.mqtt.protocol.process.ProcedureProcess;
import ext.opensource.netty.server.mqtt.protocol.process.SendMessageProcess;
import ext.opensource.netty.server.mqtt.protocol.process.TopicProcess;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProtocolProcess implements InternalRecvice {
	
	private MqttSessionService sessionService;

	private ConsumerProcess consumerProcess;
	private ProcedureProcess procedureProcess;
	private TopicProcess topicProcess;
	private SendMessageProcess sendProcess;
	
	@Setter
	private MqttAuth authService;
	@Setter
	private PubishMessageLister pubishMessageLister;
	
	public ProtocolProcess() {
	}
	
	/**
	 * init config
	 * @param cfg
	 */
	public void init(ProtocolProcessConfig cfg) {
		this.sessionService = cfg.sessionService;
		this.consumerProcess = cfg.consumerProcess;
		this.procedureProcess = cfg.procedureProcess;
		this.topicProcess = cfg.topicProcess;
		this.sendProcess = cfg.sendProcess;
		this.authService = new AuthServiceImpl();
	}

	/**
	 * process will message
	 * @param channel
	 */
	public void processWillMessage(Channel channel) {
		String clientId = NettyUtil.getClientId(channel);
		MqttPublishMessage willMsgObj = sessionService.getWillMessage(clientId);
		if (willMsgObj != null) {
			this.processPublish(channel, willMsgObj);
		}
	}

	/**
	 *  P - B, S - B
	 * @param channel
	 * @param code
	 * @param sessionPresent
	 * @param isCLose
	 */
	private void writeBackConnect(Channel channel, MqttConnectReturnCode code, Boolean sessionPresent,
			boolean isCLose) {
		this.sendProcess.sendBackConnect(channel, code, false);
		if (isCLose) {
			channel.close();
		}
	}

	/**
	 * processConnect
	 * @param channel
	 * @param msg
	 */
	public void processConnect(Channel channel, MqttConnectMessage msg) {
		// 消息解码器出现异常
		if (msg.decoderResult().isFailure()) {
			Throwable cause = msg.decoderResult().cause();
			writeBackConnect(channel, ProtocolUtil.connectReturnCodeForException(cause), false, true);
			return;
		}

		String deviceid = msg.payload().clientIdentifier();
		// clientId为空或null的情况, 这里要求客户端必须提供clientId, 不管cleanSession是否为1, 此处没有参考标准协议实现
		if (deviceid == null || deviceid.trim().length() == 0) {
			writeBackConnect(channel, MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED, false, true);
			return;
		}

		// 用户名和密码验证, 这里要求客户端连接时必须提供用户名和密码, 不管是否设置用户名标志和密码标志为1, 此处没有参考标准协议实现
		String username = msg.payload().userName();
		String password = msg.payload().passwordInBytes() == null ? null
				: new String(msg.payload().passwordInBytes(), CharsetUtil.UTF_8);
		if (!authService.checkValid(deviceid, username, password)) {
			writeBackConnect(channel, MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, false, true);
			return;
		}

		boolean isCleanSession = msg.variableHeader().isCleanSession();

		// 如果会话中已存储这个新连接的clientId, 就关闭之前该clientId的连接
		if (sessionService.containsKey(deviceid)) {
			if (isCleanSession) {
				sessionService.remove(deviceid);
				topicProcess.removeByCleanSession(deviceid);
				procedureProcess.removeByCleanSession(deviceid);
				consumerProcess.removeByCleanSession(deviceid);
			}
			sessionService.closeSession(deviceid);
		}

		// 处理遗嘱信息
		MqttSession sessionStore = new MqttSession(deviceid, channel, isCleanSession, null);
		if (msg.variableHeader().isWillFlag()) {
			MqttPublishMessage willMessage = ProtocolUtil.publishMessage(msg.payload().willTopic(), false,
					msg.variableHeader().willQos(), msg.variableHeader().isWillRetain(), 0,
					msg.payload().willMessageInBytes());
			sessionStore.setWillMessage(willMessage);
		}
		// 处理连接心跳包
		int idelTimes = msg.variableHeader().keepAliveTimeSeconds();
		if (idelTimes <= 0) {
			idelTimes = 60;
		}
		
		if (idelTimes> 0) {
			String idelStr = NettyConstant.HANDLER_NAME_HEARTCHECK;
			if (channel.pipeline().names().contains(idelStr)) {
				channel.pipeline().remove(idelStr);
			}
			channel.pipeline().addFirst(idelStr,
					new IdleStateHandler(0, 0, Math.round(idelTimes * 1.5f)));
		}

		// 至此存储会话信息及返回接受客户端连接
		sessionService.put(deviceid, sessionStore);
		channel.attr(NettyConstant.CLIENTID_KEY).set(deviceid);

		Boolean sessionPresent = sessionService.containsKey(deviceid) && !isCleanSession;
		writeBackConnect(channel, MqttConnectReturnCode.CONNECTION_ACCEPTED, sessionPresent, false);

		NettyLog.debug("CONNECT - clientId: {}, cleanSession: {}", deviceid, isCleanSession);

		// 如果cleanSession为0, 需要重发同一clientId存储的未完成的QoS1和QoS2的DUP消息
		if (!isCleanSession) {
			this.consumerProcess.processHistoryPub(channel);
			this.procedureProcess.processHistoryPubRel(channel);
		}
	}

	/**
	 *  P - B, S - B
	 * @param channel
	 * @param msg
	 */
	public void processDisConnect(Channel channel, MqttMessage msg) {
		String clientId = NettyUtil.getClientId(channel);
		boolean isCleanSession = sessionService.isCleanSession(clientId);
		NettyLog.debug("DISCONNECT - clientId: {}, cleanSession: {}", clientId, isCleanSession);

		if (isCleanSession) {
			this.topicProcess.removeByCleanSession(clientId);
			this.consumerProcess.removeByCleanSession(clientId);
			this.procedureProcess.removeByCleanSession(clientId);
		}
		sessionService.remove(clientId);
		channel.close();
	}

	/**
	 *  S - B
	 * @param channel
	 * @param msg
	 */
	public void processSubscribe(Channel channel, MqttSubscribeMessage msg) {
		String clientId = NettyUtil.getClientId(channel);
		List<MqttTopicSubscription> topicSubscriptions = msg.payload().topicSubscriptions();

		List<Integer> mqttQoSList = this.topicProcess.processTopicSubscribe(clientId, topicSubscriptions);
		if ((mqttQoSList != null) && (mqttQoSList.size() > 0)) {
			this.sendProcess.sendSubAckMessage(channel, msg.variableHeader().messageId(), mqttQoSList);
			// 发布保留消息
			topicSubscriptions.forEach(topicSubscription -> {
				String topicFilter = topicSubscription.topicName();
				MqttQoS mqttQoS = topicSubscription.qualityOfService();
				List<RetainMessage> retainMessageList = this.topicProcess.searchRetainMessage(topicFilter);
				if ((retainMessageList != null) && (retainMessageList.size() > 0)) {
					NettyLog.debug("sendRetainMessage: {}, {}", clientId, topicFilter);
					this.consumerProcess.sendRetainMessage(channel, retainMessageList, mqttQoS);
				}
			});
		} else {
			NettyLog.error("error Subscribe");
			channel.close();
		}
	}

	/**
	 *  S - B
	 * @param channel
	 * @param msg
	 */
	public void processUnSubscribe(Channel channel, MqttUnsubscribeMessage msg) {
		List<String> topicFilters = msg.payload().topics();
		String clientId = NettyUtil.getClientId(channel);

		this.topicProcess.removeClientTopic(clientId, topicFilters);
		this.sendProcess.sendUnSubAckMessage(channel, msg.variableHeader().messageId());
	}

	/**
	 *  P - B, S - B
	 * @param channel
	 * @param msg
	 */
	public void processPingReq(Channel channel, MqttMessage msg) {
		this.sendProcess.sendPingRespMessage(channel);
	}

	/**
	 *  P - B (Qos0,Qos1,Qos2)
	 * @param channel
	 * @param msg
	 */
	public void processPublish(Channel channel, MqttPublishMessage msg) {
		String clientId = NettyUtil.getClientId(channel);
		MqttQoS qosLevel = msg.fixedHeader().qosLevel();
		int packetId = msg.variableHeader().packetId();
		String topicName = msg.variableHeader().topicName();
		
		if (!topicProcess.checkVaildTopic(topicName)) {
			NettyLog.debug("In Vaild Topic: {}", topicName);
			return ;
		}
		
		boolean isRetain = msg.fixedHeader().isRetain();
		byte[] msgBytes = ByteBufUtil.copyByteBuf(msg.payload());

		BorkerMessage bMsgInfo = BorkerMessage.builder().sourceClientId(clientId).sourceMsgId(packetId)
				.topicName(topicName).iQosLevel(qosLevel.value()).msgBytes(msgBytes).retain(isRetain).build();

		NettyLog.debug("processPublish: {}", bMsgInfo);

		List<SubscribeTopicInfo> subscribeClientList = this.topicProcess.search(bMsgInfo.getTopicName());

		this.procedureProcess.processPublish(channel, bMsgInfo);
		
		if (qosLevel != MqttQoS.EXACTLY_ONCE) {
			this.consumerProcess.sendSubscribMessage(bMsgInfo, subscribeClientList);
		}

		if (qosLevel == MqttQoS.AT_MOST_ONCE) {
		} else if (qosLevel == MqttQoS.AT_LEAST_ONCE) {
			this.sendProcess.sendPubAckMessage(channel, packetId);
		} else if (qosLevel == MqttQoS.EXACTLY_ONCE) {
			this.sendProcess.sendPubRecMessage(channel, packetId);
		} else {
		}

		// retain=1, 保留消息
		if (isRetain) {
			topicProcess.publicRetainMessage(bMsgInfo);
		}
		
		if (pubishMessageLister != null) {
			pubishMessageLister.processMessage(bMsgInfo);
		}
	}

	/**
	 *  P - B (Qos2)
	 * @param channel
	 * @param variableHeader
	 */
	public void processPubRel(Channel channel, MqttMessageIdVariableHeader variableHeader) {
		int messageId = variableHeader.messageId();
		ProcedureMessage info = this.procedureProcess.processPubRel(channel, messageId);

		if (info != null) {
			NettyLog.debug("relInfo:" + info);

			BorkerMessage bMsgInfo = BorkerMessage.builder().sourceClientId(info.getSourceClientId())
					.sourceMsgId(info.getSourceMsgId()).topicName(info.getTopicName()).iQosLevel(info.getIQosLevel())
					.msgBytes(info.getMsgBytes()).retain(false).build();

			List<SubscribeTopicInfo> subscribeClientList = this.topicProcess.search(bMsgInfo.getTopicName());
			this.consumerProcess.sendSubscribMessage(bMsgInfo, subscribeClientList);
		}

		this.sendProcess.sendPubCompMessage(channel, messageId);
	}

	/**
	 *  S - B (Qos2)
	 * @param channel
	 * @param variableHeader
	 */
	public void processPubRec(Channel channel, MqttMessageIdVariableHeader variableHeader) {
		int messageId = variableHeader.messageId();
		this.consumerProcess.processPubRec(channel, messageId);
		this.sendProcess.sendPubRelMessage(channel, messageId, false);
	}

	/**
	 *  S - B (Qos1)
	 * @param channel
	 * @param variableHeader
	 */
	public void processPubAck(Channel channel, MqttMessageIdVariableHeader variableHeader) {
		int messageId = variableHeader.messageId();
		this.consumerProcess.processPubAck(channel, messageId);
	}

	/**
	 *  S - B (Qos2)
	 * @param channel
	 * @param variableHeader
	 */
	public void processPubComp(Channel channel, MqttMessageIdVariableHeader variableHeader) {
		int messageId = variableHeader.messageId();
		this.consumerProcess.processPubComp(channel, messageId);
	}
	

	/**
	 * 内部交互消息处理
	 */
	@Override
	public boolean processInternalRecvice(InternalMessage msg) {
		NettyLog.debug("processInternalRecvice");
		consumerProcess.sendSubscribMessageForInternal(msg);
		return true;
	}
}
