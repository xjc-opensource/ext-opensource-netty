package ext.opensource.netty.server.mqtt.protocol.data;

import ext.opensource.netty.server.mqtt.common.RetainMessage;
import ext.opensource.netty.server.mqtt.common.SubscribeTopicInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class TopicData extends BaseDataInMap<String, SubscribeTopicInfo>{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	public RetainMessage retainMessageInfo;
	
	public TopicData() {
		
	}
	public TopicData(String name) {
		super(name);
	}
}
