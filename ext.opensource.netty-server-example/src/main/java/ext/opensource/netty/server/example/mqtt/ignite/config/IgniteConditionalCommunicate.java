
package ext.opensource.netty.server.example.mqtt.ignite.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class IgniteConditionalCommunicate implements Condition {
    @Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    	Environment env = context.getEnvironment();
        return "ignite".equalsIgnoreCase(env.getProperty("netty.server.interal.communicate"));
    }
}
