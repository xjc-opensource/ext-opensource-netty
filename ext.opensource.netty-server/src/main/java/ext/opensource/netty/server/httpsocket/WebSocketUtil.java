package ext.opensource.netty.server.httpsocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.ssl.SslHandler;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class WebSocketUtil {
	public static ChannelFuture sendTextMessage(ChannelHandlerContext ctx, String msg) {
		return ctx.writeAndFlush(new TextWebSocketFrame(msg));
	}
	public static ChannelFuture sendBinnaryMessage(ChannelHandlerContext ctx, ByteBuf msg){
		return ctx.writeAndFlush(new BinaryWebSocketFrame(msg));
	}
	
	public static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
		String protocol = "ws";
		if (cp.get(SslHandler.class) != null) {
			// SSL in use so use Secure WebSockets
			protocol = "wss";
		}
		return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
	}
}
