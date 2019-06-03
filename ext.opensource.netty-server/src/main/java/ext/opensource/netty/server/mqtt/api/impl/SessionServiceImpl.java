package ext.opensource.netty.server.mqtt.api.impl;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.CacheListLocalMemory;
import ext.opensource.netty.server.mqtt.MqttSession;
import ext.opensource.netty.server.mqtt.api.MqttSessionService;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class SessionServiceImpl implements MqttSessionService {
	private CacheList<MqttSession> localCache = new CacheListLocalMemory<MqttSession>();
	
	private CacheList<MqttSession> remoteCache = null;

	@Override
	public void setRemoteSessionCache(CacheList<MqttSession> cacheList) {
		if (cacheList != null) {
			this.remoteCache = cacheList;
		}	
	}
	
	@Override
	public void put(String clientId, MqttSession sessionStore) {
		localCache.put(clientId, sessionStore);
		if (remoteCache != null) {
			remoteCache.put(clientId, sessionStore);
		}
	}

	@Override
	public MqttSession getSession(String clientId) {
		return localCache.get(clientId);
	}

	@Override
	public boolean containsKey(String clientId) {
		return localCache.containsKey(clientId);
	}

	@Override
	public void remove(String clientId) {
		if (clientId != null) {
			localCache.remove(clientId);
			
			if (remoteCache != null) {
				remoteCache.remove(clientId);
			}
		}
	}

	@Override
	public void writeAndFlush(String clientId, Object obj) {
		MqttSession session = localCache.get(clientId);
		if (null != session) {
			session.channel().writeAndFlush(obj);
		}
	}

	@Override
	public boolean isCleanSession(String clientId) {
		MqttSession session = localCache.get(clientId);
		if (null != session) {
			return session.isCleanSession();
		} else {
			return false;
		}
	}

	@Override
	public void closeSession(String clientId) {
		MqttSession session = localCache.get(clientId);
		if (null != session) {
			session.close();
		}
	}

	@Override
	public MqttPublishMessage getWillMessage(String clientId) {
		MqttSession session = localCache.get(clientId);
		if (null != session) {
			return session.getWillMessage();
		} else {
			return null;
		}
	}
}
