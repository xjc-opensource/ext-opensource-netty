package ext.opensource.netty.server.example.mqtt;

import com.alibaba.fastjson.JSON;
import ext.opensource.netty.server.mqtt.MqttServer;
import ext.opensource.netty.server.mqtt.api.InternalSend;
import ext.opensource.netty.server.mqtt.common.InternalMessage;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttCustomCommunicate extends BaseMqttCustom implements InternalSend, MqttServerRecvice {	
	@Override
	public void init(MqttServer mqttServer) {
		super.init(mqttServer);
		mqttServer.initMqtt().setInternalSend(this);
	}

	@Override
	public void internalSend(InternalMessage msg) {	
		processInternalSend(JSON.toJSONString(msg));
	}
	protected void processInternalSend(String msg) {	
	}

	public boolean processInternalRecvice(InternalMessage msg) {	
		return mqttServer.internalRecvice().processInternalRecvice(msg);
	}

	@Override
	public boolean processServerRecviceMesage(String message) {
		InternalMessage msgObj = JSON.parseObject(message, InternalMessage.class);
		return processInternalRecvice(msgObj);
	}
}
