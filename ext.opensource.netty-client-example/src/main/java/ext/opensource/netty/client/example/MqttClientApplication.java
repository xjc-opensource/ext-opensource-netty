package ext.opensource.netty.client.example;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ext.opensource.netty.client.example.listen.SpringBeanUtil;
import ext.opensource.netty.client.example.mqtt.MqttClientCustomInit;
import ext.opensource.netty.client.mqtt.MqttClient;
import ext.opensource.netty.client.mqtt.api.MqttConsumerListener;
import ext.opensource.netty.client.mqtt.common.ClientEvent;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.api.SocketApplication;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@SpringBootApplication
public class MqttClientApplication implements CommandLineRunner, SocketApplication {
	private MqttClient nettyClient;

	public static void main(String[] args) {
		SpringApplication.run(MqttClientApplication.class, args);
	}

	@Override
	public void run(String... strings) throws URISyntaxException {
		String clientId = "JavaClient";
		nettyClient = new MqttClient();
		
		///ssl
		//nettyClient.setSslCtx(SslContextUtil.createSSLClientContextForJKS("cert/ssl_client.jks", "client"));
		
		nettyClient.mqttOptions().setClientIdentifier(clientId);
		initCustom(nettyClient);
		
		nettyClient.setLoginSuccess(new ClientEvent() {
			@Override
			public void process() {
				nettyClient.consumer().subscribe("/a", 2);
				///
				nettyClient.producer().sendPubishMessage("/a", "demo", 2, false);
			}
		});
		
		nettyClient.consumer().setConsumerListener(new MqttConsumerListener(){
			@Override
			public void receiveMessage(int msgId, String topic, String msg) {
				// TODO Auto-generated method stub	
				System.err.println(String.format("msgid:%s, topic: %s, msg: %s", msgId, topic, msg));
			}

			@Override
			public void receiveMessageByAny(int msgId, String topic, String msg) {
				// TODO Auto-generated method stub
			}
		});

		///nettyClient.setCharsetName("GB2312");
		///nettyClient.setCheckConnectFlag(true);
		///nettyClient.connect("192.168.136.148", 1883);
		///
		 nettyClient.connect(8989);
		// nettyClient.requireSync();
		System.err.println("mqtt client run end");
	}
	
	private void initCustom(MqttClient client) {
		Map<String, MqttClientCustomInit> result = SpringBeanUtil.getApplicationContext().getBeansOfType(MqttClientCustomInit.class);
		for (Entry<String, MqttClientCustomInit> app : result.entrySet()) {
			NettyLog.info("init custom - " + app.getKey());
			app.getValue().init(client);;
		}	
	}

	@Override
	public void shutdown() {
		if (nettyClient != null) {
			nettyClient.shutdown();
			nettyClient = null;
		}
	}
}
