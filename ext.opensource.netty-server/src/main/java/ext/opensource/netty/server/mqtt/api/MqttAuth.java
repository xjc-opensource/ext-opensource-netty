
package ext.opensource.netty.server.mqtt.api;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttAuth {
	/**
	 * 验证接入终端信息
	 * @param clientId
	 * @param username
	 * @param password
	 * @return
	 */
	boolean checkValid(String clientId, String username, String password);
}
