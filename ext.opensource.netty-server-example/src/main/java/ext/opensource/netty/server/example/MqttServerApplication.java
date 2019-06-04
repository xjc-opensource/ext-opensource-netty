package ext.opensource.netty.server.example;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.api.SocketApplication;
import ext.opensource.netty.common.utils.SslContextUtil;
import ext.opensource.netty.server.example.listen.SpringBeanUtil;
import ext.opensource.netty.server.example.mqtt.MqttCustom;
import ext.opensource.netty.server.example.mqtt.MqttCustomInit;
import ext.opensource.netty.server.mqtt.MqttServer;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@SpringBootApplication
public class MqttServerApplication implements CommandLineRunner, SocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(MqttServerApplication.class, args);
	}
	
	private MqttServer mqttServer;

	@Override
	public void run(String... strings) {
		mqttServer = new MqttServer();
		
		///ssl单向
	    mqttServer.setSslCtx(SslContextUtil.createSSLServerContextForJKS("cert/ssl_server.jks", "server"));
		mqttServer.setSslClientAuth(false);
		
		///ssl双向
	    //mqttServer.setSslCtx(SslContextUtil.createSSLServerContextForJKS("cert/ssl_server_both.jks", "server"));
		//mqttServer.setSslClientAuth(true);
		
		
		initCustom(mqttServer);
		mqttServer.bind(8989);
		///mqttServer.requireSync();
		///mqttServer.testInternalMessage();
		NettyLog.info("mqtt server run end "); 
	}
	
	private void initCustom(MqttServer mqttServer) {
		MqttCustom mqttCustom = new MqttCustom();
		mqttCustom.init(mqttServer);
		
		Map<String, MqttCustomInit> result = SpringBeanUtil.getApplicationContext().getBeansOfType(MqttCustomInit.class);
		for (Entry<String, MqttCustomInit> app : result.entrySet()) {
			NettyLog.info("init custom - " +app.getKey());
			app.getValue().init(mqttServer);;
		}
	}
	
	@Override
	public void shutdown() {
		if (mqttServer != null) {
			mqttServer.shutdown();
			mqttServer = null;
		}
	}
}