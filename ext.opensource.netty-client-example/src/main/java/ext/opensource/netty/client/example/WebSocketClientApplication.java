package ext.opensource.netty.client.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import ext.opensource.netty.client.httpsocket.WebSocketClient;
import ext.opensource.netty.common.api.SocketApplication;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

//@SpringBootApplication

public class WebSocketClientApplication implements CommandLineRunner, SocketApplication {
	private WebSocketClient webScoketClient;

	public static void main(String[] args) {
		SpringApplication.run(WebSocketClientApplication.class, args);
	}

	@Override
	public void run(String... strings) {
		WebSocketClient webScoketClient = new WebSocketClient("/websocket");
		webScoketClient.setCheckConnectFlag(true);
		webScoketClient.connect(8989);
		webScoketClient.requireSync();
		webScoketClient.sendMessage("test");
		System.err.println("websocket client run end");
	}

	@Override
	public void shutdown() {
		if (webScoketClient != null) {
			webScoketClient.shutdown();
		}
	}
}
