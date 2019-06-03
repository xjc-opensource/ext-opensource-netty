package ext.opensource.netty.client.mqtt.common;

import io.netty.handler.codec.mqtt.MqttVersion;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Getter
@Setter
@Data
public class MqttConnectOptions {
	private MqttVersion mqttVersion = MqttVersion.MQTT_3_1_1;

	private String clientIdentifier = "";
	private String willTopic=""; 
	private String userName="";
	private byte[] willMessage;
	private byte[] password;
	private int willQos=0;
	private int keepAliveTime=60;

	private boolean hasUserName = false;
	private boolean hasPassword = false;
	private boolean hasWillRetain = false;
	private boolean hasWillFlag = false;
	private boolean hasCleanSession = false;

	public MqttConnectOptions() {

	}
}
