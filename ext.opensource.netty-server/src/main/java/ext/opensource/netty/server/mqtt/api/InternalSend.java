package ext.opensource.netty.server.mqtt.api;

import ext.opensource.netty.server.mqtt.common.InternalMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface InternalSend {
	/**
	 * 发送内部信息
	 * @param msg
	 */
	public void internalSend(InternalMessage msg);
}
