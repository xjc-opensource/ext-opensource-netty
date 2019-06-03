package ext.opensource.netty.server.simple;

import ext.opensource.netty.common.TranDataProto;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface SocketCmdEvent {
	/**
	 * doCmd
	 * @param code
	 * @param message
	 * @return
	 */
	public TranDataProto doCmd(String code, String message);
}
