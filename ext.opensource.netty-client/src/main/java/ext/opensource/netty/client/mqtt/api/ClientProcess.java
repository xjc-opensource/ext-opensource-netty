package ext.opensource.netty.client.mqtt.api;

import java.nio.charset.Charset;

import ext.opensource.netty.client.core.BaseClient;
import ext.opensource.netty.common.NettyUtil;
import ext.opensource.netty.common.api.GlobalUniqueId;
import ext.opensource.netty.common.api.GlobalUniqueIdImpl;
import ext.opensource.netty.common.api.GlobalUniqueIdSet;
import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.common.core.UniqueIdInteger;

import io.netty.channel.Channel;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ClientProcess implements GlobalUniqueIdSet {
	private BaseClient client;
	private GlobalUniqueId globalUniqueId;

	public ClientProcess(BaseClient client) {
		globalUniqueId = new GlobalUniqueIdImpl();
		this.client = client;
	}
	
	@Override
	public void setGlobalUniqueId(GlobalUniqueId globalUniqueId) {
		if (globalUniqueId != null) {
			this.globalUniqueId = globalUniqueId;
		}
	}
	@Override
	public void setGlobalUniqueIdCache(CacheList<UniqueIdInteger> cacheList) {
		globalUniqueId.setCacheList(cacheList);
	}
	
	protected String getClientId() {
		return NettyUtil.getClientId(client.getChannel());
	}

	public Channel channel() {
		return client.getChannel();
	}

	public GlobalUniqueId messageId() {
		return globalUniqueId;
	}

	public byte[] encoded(String data) {
		if (data == null) {
			return null;
		}
		return data.getBytes(Charset.forName(this.client.getCharsetName()));
	}

	public String decode(byte[] data) {
		return new String(data, Charset.forName(this.client.getCharsetName()));
	}
}
