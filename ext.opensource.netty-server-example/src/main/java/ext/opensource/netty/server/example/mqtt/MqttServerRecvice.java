package ext.opensource.netty.server.example.mqtt;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttServerRecvice {
	/**
	 * 处理订阅到的信息
	 * @param message
	 * @return
	 */
	public boolean processServerRecviceMesage(String message);
}
