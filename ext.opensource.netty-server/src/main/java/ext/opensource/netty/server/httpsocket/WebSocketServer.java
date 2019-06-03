package ext.opensource.netty.server.httpsocket;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class WebSocketServer extends HttpServer {
	@Getter
	@Setter
	@NonNull
	private WebSocketEvent webSocketEvent;

	@Setter 
	private String websocketPath;

	public WebSocketServer() {
	}

	@Override
	public void broadcastMessageString(String msg) {
		super.broadcastMessage(new TextWebSocketFrame(msg));
	}

	@Override
	protected void processHttpHandler(HttpServerHandler httpServerHandler) {
		if (httpServerHandler != null) {
			httpServerHandler.setWebSocketEvent(webSocketEvent);
			httpServerHandler.setWebsocketPath(websocketPath);
		}
	}
}
