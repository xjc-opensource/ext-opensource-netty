package ext.opensource.netty.server.mqtt.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class ConsumerMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sourceClientId;
	private String topic;
	private int mqttQoS;
	private byte[] messageBytes;
	private int messageId;
	private boolean retain;
	private boolean dup;
}
