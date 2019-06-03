package ext.opensource.netty.server.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.api.SocketApplication;
import ext.opensource.netty.server.example.http.HttpResourceThymeleaf;
import ext.opensource.netty.server.example.websocket.WebSocketEventChat;
import ext.opensource.netty.server.httpsocket.BaseHttpResource;
import ext.opensource.netty.server.httpsocket.WebSocketServer;


/**
 * @author ben
 * @Title: basic
 * @Description:
 * 
 **/

//@SpringBootApplication

public class WebSocketApplication implements CommandLineRunner, SocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebSocketApplication.class, args);
	}

	private WebSocketServer webScoketServer;
 
	///127.0.0.1:8989/ websocket客户端
	
	@Override
	public void run(String... strings) {
		BaseHttpResource httpResource = new HttpResourceThymeleaf();
		httpResource.setRootDir("static/");
		httpResource.setDefaultIndexName("websocket.html");
		
		webScoketServer = new WebSocketServer();
		webScoketServer.setHttpResource(httpResource);
		
		webScoketServer.setWebsocketPath("/wss");
		webScoketServer.setWebSocketEvent(new WebSocketEventChat());
		webScoketServer.bind(8989);
		
		NettyLog.info("websocket server run end "); 
	}

	@Override
	public void shutdown() {
		if (webScoketServer != null) {
			webScoketServer.shutdown();
		}
	}
}