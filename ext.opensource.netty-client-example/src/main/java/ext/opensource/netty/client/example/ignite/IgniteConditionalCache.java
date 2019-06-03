
package ext.opensource.netty.client.example.ignite;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ben
 * @Title: xxx.java
 * @Description:
 **/

public class IgniteConditionalCache implements Condition {
    @Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    	Environment env = context.getEnvironment();
        return "ignite".equalsIgnoreCase( env.getProperty("netty.client.cache"));
    }
}
