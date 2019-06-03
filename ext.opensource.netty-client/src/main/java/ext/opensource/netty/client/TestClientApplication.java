package ext.opensource.netty.client;

import java.net.URISyntaxException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ext.opensource.netty.client.mqtt.MqttClient;
import ext.opensource.netty.client.mqtt.api.MqttConsumerListener;
import ext.opensource.netty.common.api.SocketApplication;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@SpringBootApplication
public class TestClientApplication implements CommandLineRunner, SocketApplication {
	private MqttClient nettyClient;

	public static void main(String[] args) {
		SpringApplication.run(TestClientApplication.class, args);
	}

	@Override
	public void run(String... strings) throws URISyntaxException {
		nettyClient = new MqttClient();
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
		
		nettyClient.setCharsetName("GB2312"); 
		nettyClient.setCheckConnectFlag(true);
		///nettyClient.connect("192.168.136.148", 1883);
		nettyClient.connect(8989);
		nettyClient.requireSync();
		///
		//nettyClient.consumer().subscribe("/a", 2);
		//nettyClient.consumer().subscribe("/a", 1);
		//nettyClient.producer().sendPubishMessage("/a", "demo", 2, false);
		System.err.println("client run end");
	}

	@Override
	public void shutdown() {
		if (nettyClient != null) { 
			nettyClient.shutdown();
		}
	}
}
