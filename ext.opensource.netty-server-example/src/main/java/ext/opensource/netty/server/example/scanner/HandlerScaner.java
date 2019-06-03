package ext.opensource.netty.server.example.scanner;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class HandlerScaner implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		//System.err.println("postProcessBeforeInitialization: " + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		Class<? extends Object> clazz = bean.getClass();
		
		Class<?>[] interfaces = clazz.getInterfaces();
		
		if(interfaces != null && interfaces.length > 0){
/*			//扫描类的所有接口父类
			for (Class<?> interFace : interfaces) {
				//判断是否为handler接口类
				SocketModule socketModule = interFace.getAnnotation(SocketModule.class);
				if (socketModule == null) {
					continue;
				}
				
				//找出命令方法
				Method[] methods = interFace.getMethods();
				if(methods != null && methods.length > 0){
					for(Method method : methods){
						SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
						if(socketCommand == null){
							continue;
						}
						
						final short module = socketModule.module();
						final short cmd = socketCommand.cmd();
						
						if(InvokerHoler.getInvoker(module, cmd) == null){
							InvokerHoler.addInvoker(module, cmd, Invoker.valueOf(method, bean));
						}else{
							System.out.println("重复命令:"+"module:"+module +" "+"cmd：" + cmd);
						}
					}
				}
				
			}*/
		}
		return bean;
	}

}
