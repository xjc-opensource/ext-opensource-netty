package ext.opensource.netty.server.example;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.api.SocketApplication;
import ext.opensource.netty.server.example.http.HttpResourceThymeleaf;
import ext.opensource.netty.server.example.websocket.WebSocketEventChat;
import ext.opensource.netty.server.httpsocket.BaseHttpResource;
import ext.opensource.netty.server.httpsocket.HttpResourceProcess;
import ext.opensource.netty.server.httpsocket.WebSocketServer;
import ext.opensource.netty.server.httpsocket.WebSocketUtil;

import io.netty.handler.codec.http.HttpRequest;


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
		String websocketPath = "/wss";
		webScoketServer = new WebSocketServer();
		
		BaseHttpResource httpResource = new HttpResourceThymeleaf();
		httpResource.setRootDir("static/websocket/");
		httpResource.setDefaultIndexName("websocket.html");
		httpResource.setHttpResourceProcess(new HttpResourceProcess() {
			@Override
			public void porcessResPath(HttpRequest req, String reqPath,
					Map<String, Object> reqParameter) {
				if  (httpResource.getDefaultIndexName().equalsIgnoreCase(reqPath) && (reqParameter != null)) {
					reqParameter.put("socketurl", WebSocketUtil.getWebSocketLocation(webScoketServer.getSslCtx() != null, req, websocketPath));
				}
			}
		});
		

		webScoketServer.setHttpResource(httpResource);
		webScoketServer.setWebsocketPath(websocketPath);
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