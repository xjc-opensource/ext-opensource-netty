package ext.opensource.netty.server.mqtt.common;

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
public class InternalMessage {
	private String topicName;
	private int respQoS;
	private byte[] msgBytes;
	
	//@Builder.Default
	//private int borkerMsgId = -1;
	
	@Builder.Default
	private boolean retain = false;
	@Builder.Default
	private boolean dup = false;
	
	private String destClientId;
}
