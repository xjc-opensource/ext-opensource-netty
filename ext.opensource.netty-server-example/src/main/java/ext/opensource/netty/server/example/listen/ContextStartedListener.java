package ext.opensource.netty.server.example.listen;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class ContextStartedListener implements ApplicationListener<ContextStartedEvent>{
	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		System.err.println("============Start 执行=========== ");
	}
}