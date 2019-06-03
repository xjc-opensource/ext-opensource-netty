package ext.opensource.netty.server.example.mqtt.ignite;

import javax.annotation.PostConstruct;
import org.apache.ignite.IgniteMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import ext.opensource.netty.server.mqtt.common.BorkerMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class InternalCommunicationIgnite {
	private final String internalTopic = "internal-ignite-topic";

	@Autowired
	private IgniteMessaging igniteMessaging;

	@PostConstruct
	private void internalListen() {
		igniteMessaging.localListen(internalTopic, (nodeId, msg) -> {
			///InternalMessage internalMessage = (InternalMessage) msg;
			return true;
		});
	}

	public void internalSend(BorkerMessage bMsg) {
		if (igniteMessaging.clusterGroup().nodes() != null && igniteMessaging.clusterGroup().nodes().size() > 0) {
			igniteMessaging.send(internalTopic, bMsg);
		}
	}


}
