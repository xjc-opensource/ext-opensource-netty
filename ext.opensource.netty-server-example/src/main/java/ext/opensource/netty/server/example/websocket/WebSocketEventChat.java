package ext.opensource.netty.server.example.websocket;

import java.util.Map;

import ext.opensource.netty.server.core.BaseServer;
import ext.opensource.netty.server.httpsocket.BaseWebSocketEvent;
import ext.opensource.netty.server.httpsocket.WebSocketSession;

import io.netty.buffer.ByteBuf;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class WebSocketEventChat extends BaseWebSocketEvent {
	@Override
	public void onMessageStringEvent(BaseServer sevice, WebSocketSession session, String msg) {
		String msgStr = msg + " < " + session.id().asShortText();
		System.out.println("recv msg:" + msgStr);
		sevice.broadcastMessageString(msgStr);
	}

	@Override
	public void onMessageBinaryEvent(BaseServer sevice, WebSocketSession session, ByteBuf msg) {
		session.sendBinaryMessage(msg);
	}

	@Override
	public void onOpenEvent(BaseServer sevice, WebSocketSession session, Map<String, Object> parameter) {
		System.err.println("onOpenEvent parameter:" + parameter);
		sevice.broadcastMessageString("weclome " + session.channel().id().asShortText());
	}

	@Override
	public void onCloseEvent(BaseServer sevice, WebSocketSession ctx) {
		System.err.println("close");
	}
}
