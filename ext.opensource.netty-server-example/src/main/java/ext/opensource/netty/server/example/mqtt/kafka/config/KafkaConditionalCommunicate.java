
package ext.opensource.netty.server.example.mqtt.kafka.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class KafkaConditionalCommunicate implements Condition {
	@Override
	public boolean matches(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		Environment env = context.getEnvironment();
		return "kafka".equalsIgnoreCase(
				env.getProperty("netty.server.interal.communicate"));
	}
}
