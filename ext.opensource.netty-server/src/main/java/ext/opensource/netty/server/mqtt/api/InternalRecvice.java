package ext.opensource.netty.server.mqtt.api;

import ext.opensource.netty.server.mqtt.common.InternalMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface InternalRecvice {
	/**
	 * 接收内部信息处理
	 * @param msg
	 * @return
	 */
	public boolean processInternalRecvice(InternalMessage msg);
}