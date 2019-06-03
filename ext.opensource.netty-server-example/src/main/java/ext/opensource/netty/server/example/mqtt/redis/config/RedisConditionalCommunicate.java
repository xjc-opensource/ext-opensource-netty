
package ext.opensource.netty.server.example.mqtt.redis.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ben
 * @Title: xxx.java
 * @Description:
 **/

public class RedisConditionalCommunicate implements Condition {
    @Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    	Environment env = context.getEnvironment();
    	return "redis".equalsIgnoreCase(env.getProperty("netty.server.interal.communicate"));
    }
}
