package ext.opensource.netty.server.example.listen;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class ContextStopListener implements ApplicationListener<ContextStoppedEvent>{
	@Override
	public void onApplicationEvent(ContextStoppedEvent event) {
		System.err.println("============Stop 执行=========== ");
	}
}