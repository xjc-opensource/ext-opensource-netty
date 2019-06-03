package ext.opensource.netty.client.core;

import ext.opensource.netty.common.exception.LoginException;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface ClientProcess {
	/**
	 * 登录完成
	 * @param bResult
	 * @param exception
	 */
	void loginFinish(boolean bResult, LoginException exception);
	
	/**
	 * 发送指令关闭
	 */
	void disConnect();
}
