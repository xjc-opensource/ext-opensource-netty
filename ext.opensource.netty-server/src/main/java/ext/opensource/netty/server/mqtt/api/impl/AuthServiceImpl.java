package ext.opensource.netty.server.mqtt.api.impl;

import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.server.mqtt.api.MqttAuth;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class AuthServiceImpl implements MqttAuth{
	@Override
	public boolean checkValid(String deviceId, String username, String password) {
		NettyLog.debug("AuthServiceImpl");
		return true;
	}
	
}
