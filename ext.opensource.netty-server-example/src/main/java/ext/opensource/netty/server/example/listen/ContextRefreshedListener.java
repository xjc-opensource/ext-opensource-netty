package ext.opensource.netty.server.example.listen;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import ext.opensource.netty.server.example.simple.BizDispatchCollect;
import ext.opensource.netty.server.example.simple.SocketCmd;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	System.err.println("refresh");
		
		Map<String, Object> map = new HashMap<>(16);
        Map<String, Object> bizMap = event.getApplicationContext().getBeansWithAnnotation(SocketCmd.class);
        for (Map.Entry<String, Object> entry : bizMap.entrySet()) {
            Object object = entry.getValue();
            Class<? extends Object> c = object.getClass();
            Annotation[] annotations = c.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(SocketCmd.class)) {
                	SocketCmd cmdOrder = (SocketCmd) annotation;
                	if (!map.containsKey(cmdOrder.value())) {
                		map.put(cmdOrder.value(), object);
                	} else {
                		System.err.println(String.format("Mult cmdOrder:%s className: %s", cmdOrder.value(), c));
                	}
                }
            }
        }
     
        BizDispatchCollect.setCourses(map);
    }
}
