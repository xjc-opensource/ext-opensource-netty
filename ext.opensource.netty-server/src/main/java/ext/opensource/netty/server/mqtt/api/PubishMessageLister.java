package ext.opensource.netty.server.mqtt.api;

import ext.opensource.netty.server.mqtt.common.BorkerMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/


public interface PubishMessageLister {
	/**
	 * custom define process
	 * @param msg
	 */
	void processMessage(BorkerMessage msg);
}
