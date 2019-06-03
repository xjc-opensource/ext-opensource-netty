
package ext.opensource.netty.client.example.redis;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ben
 * @Title: xxx.java
 * @Description:
 **/

public class RedisConditionalCache implements Condition {
    @Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    	Environment env = context.getEnvironment();
        return "redis".equalsIgnoreCase( env.getProperty("netty.client.cache"));
    }
}
