package ext.opensource.netty.server.mqtt.protocol;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.UniqueIdInteger;
import ext.opensource.netty.server.mqtt.MqttSession;
import ext.opensource.netty.server.mqtt.api.ConsumerDataService;
import ext.opensource.netty.server.mqtt.api.CustomConfig;
import ext.opensource.netty.server.mqtt.api.InternalSend;
import ext.opensource.netty.server.mqtt.api.MqttAuth;
import ext.opensource.netty.server.mqtt.api.MqttSessionService;
import ext.opensource.netty.server.mqtt.api.ProcedureDataService;
import ext.opensource.netty.server.mqtt.api.PubishMessageLister;
import ext.opensource.netty.server.mqtt.api.TopicService;
import ext.opensource.netty.server.mqtt.api.impl.ConsumerDataServiceImpl;
import ext.opensource.netty.server.mqtt.api.impl.ProcedureDataServiceImpl;
import ext.opensource.netty.server.mqtt.api.impl.SessionServiceImpl;
import ext.opensource.netty.server.mqtt.api.impl.TopicServiceImpl;
import ext.opensource.netty.server.mqtt.protocol.data.ClientTopic;
import ext.opensource.netty.server.mqtt.protocol.data.ConsumerClientData;
import ext.opensource.netty.server.mqtt.protocol.data.ProcedureClientData;
import ext.opensource.netty.server.mqtt.protocol.data.TopicData;
import ext.opensource.netty.server.mqtt.protocol.process.ConsumerProcess;
import ext.opensource.netty.server.mqtt.protocol.process.ProcedureProcess;
import ext.opensource.netty.server.mqtt.protocol.process.SendMessageProcess;
import ext.opensource.netty.server.mqtt.protocol.process.TopicProcess;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProtocolProcessConfig implements CustomConfig {
	private ProtocolProcess protocolProcess;
	public MqttSessionService sessionService;
	public ConsumerProcess consumerProcess;
	public ProcedureProcess procedureProcess;
	public TopicProcess topicProcess;
	public SendMessageProcess sendProcess;
	
	private ConsumerDataService consumerData;
	private ProcedureDataService procedureData;
	private TopicService topicData;

	public ProtocolProcessConfig(ProtocolProcess protocolProcess) {
		this.protocolProcess = protocolProcess;

		consumerData = new ConsumerDataServiceImpl();
		procedureData = new ProcedureDataServiceImpl();
		topicData = new TopicServiceImpl();
		
		sessionService = new SessionServiceImpl();
		sendProcess = new SendMessageProcess();
		topicProcess = new TopicProcess(topicData);
		consumerProcess = new ConsumerProcess(sendProcess, consumerData, sessionService);
		procedureProcess = new ProcedureProcess(sendProcess, procedureData);
	}

	@Override
	public void setInternalSend(InternalSend internalSend) {
		consumerProcess.setInternalSend(internalSend);
	}
	
	@Override
	public void setAuthService(MqttAuth authService) {
		if (authService == null) {
			return;
		} else {
			protocolProcess.setAuthService(authService);
		}
	}

	@Override
	public void setConsumerCacheList(CacheList<ConsumerClientData> cacheList) {
		this.consumerData.setConsumerCacheList(cacheList);
	}

	@Override
	public void setProcedureCacheList(CacheList<ProcedureClientData> cacheList) {
		this.procedureData.setProcedureCacheList(cacheList);
	}

	@Override
	public void setTopicList(CacheList<TopicData> cacheList) {
		this.topicData.setTopicList(cacheList);
	}

	@Override
	public void setClientTopicList(CacheList<ClientTopic> cacheList) {
		this.topicData.setClientTopicList(cacheList);
	}

	@Override
	public void setGlobalUniqueIdCacheList(CacheList<UniqueIdInteger> cacheList) {
		this.consumerProcess.setGlobalUniqueIdCacheList(cacheList);
	}

	@Override
	public void setPubishMessageLister(PubishMessageLister msgLister) {
		protocolProcess.setPubishMessageLister(msgLister);
	}
	
	@Override
	public void setRemoteSessionCache(CacheList<MqttSession> cacheList) {
		this.sessionService.setRemoteSessionCache(cacheList);
	}
}
