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
public class BorkerMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sourceClientId;
	private int sourceMsgId;

	private String topicName;
	private int iQosLevel;
	private byte[] msgBytes;
	
	@Builder.Default
	private boolean retain = false;
	
	@Builder.Default
	private boolean dup = false;
	
	@Builder.Default
	private long timestamp = System.currentTimeMillis();
}
