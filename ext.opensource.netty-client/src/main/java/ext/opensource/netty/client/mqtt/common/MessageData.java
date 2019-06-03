package ext.opensource.netty.client.mqtt.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String topic;
	private byte[] payload;
	
	private int qos;
	private boolean retained;
	private boolean dup;
	private int messageId;
	
	@Builder.Default
	private long timestamp = System.currentTimeMillis();
	
	private volatile MessageStatus status;
	
	public String getStringId() {
		return String.valueOf(messageId);
	}
}
