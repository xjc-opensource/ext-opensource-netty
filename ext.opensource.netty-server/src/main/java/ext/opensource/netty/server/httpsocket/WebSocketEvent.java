package ext.opensource.netty.server.httpsocket;

import java.util.Map;

import ext.opensource.netty.server.core.BaseServer;
import io.netty.buffer.ByteBuf;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface WebSocketEvent {
	/**
	 * onOpenEvent
	 * @param sevice
	 * @param session
	 * @param parameter
	 */
	public void onOpenEvent(BaseServer sevice, WebSocketSession session, Map<String, Object> parameter);
	
	/**
	 * onCloseEvent
	 * @param sevice
	 * @param ctx
	 */
	public void onCloseEvent(BaseServer sevice, WebSocketSession ctx);
	
	/*public void OnErrorEvent(WebSocketSession wsSession);*/
	
	/**
	 * onMessageStringEvent
	 * @param sevice
	 * @param session
	 * @param msg
	 */
	public void onMessageStringEvent(BaseServer sevice, WebSocketSession session, String msg);
	
	/**
	 * onMessageBinaryEvent
	 * @param sevice
	 * @param session
	 * @param msg
	 */
	public void onMessageBinaryEvent(BaseServer sevice, WebSocketSession session, ByteBuf msg);
}

