package ext.opensource.netty.client.example.listen;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import ext.opensource.netty.common.api.SocketApplication;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent>{
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.err.println("============Close 执行=========== ");

		Map<String, SocketApplication> result = event.getApplicationContext().getBeansOfType(SocketApplication.class);

		for (Entry<String, SocketApplication> app : result.entrySet()) {
			System.err.println(app.getKey());
			app.getValue().shutdown();
		}
		
	}
}