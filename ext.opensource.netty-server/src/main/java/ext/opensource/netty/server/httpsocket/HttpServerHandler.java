package ext.opensource.netty.server.httpsocket;

import static ext.opensource.netty.common.NettyConstant.WEBSOCKET_KEY;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;

import java.util.HashMap;
import java.util.Map;

import ext.opensource.netty.server.core.BaseServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.Attribute;
import io.netty.util.CharsetUtil;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {
	private WebSocketServerHandshaker handshaker;
	private BaseHttpResource httpResource;
	private BaseServer baseServer;
	
	@Setter
	private WebSocketEvent webSocketEvent;
	@Setter
	private String websocketPath;

	public HttpServerHandler(BaseServer baseServer, BaseHttpResource httpResource) {
		this.baseServer = baseServer;
		this.httpResource = httpResource;
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (webSocketEvent != null) {
			if (ctx.channel().hasAttr(WEBSOCKET_KEY)) {
				webSocketEvent.onCloseEvent(baseServer, new WebSocketSession(ctx.channel()));
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		// Handle a bad request.
		if (!req.decoderResult().isSuccess()) {
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}

		// Allow only GET methods.
		if (req.method() != HttpMethod.GET) {
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
			return;
		}

		String uri = req.uri();
		int index = uri.indexOf("?");
		String paramterStr = "";
		String path = null;
		if (index == -1) {
			path = uri;
		} else {
			path = uri.substring(0, index);
			paramterStr = uri.substring(index+1);
		}

	    if (websocketPath != null && websocketPath.trim().length() > 0 && websocketPath.equals(path)) {
			// Handshake
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
					WebSocketUtil.getWebSocketLocation(ctx.pipeline(), req, websocketPath), null, true, 5 * 1024 * 1024);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
					
				if (!ctx.channel().hasAttr(WEBSOCKET_KEY)) {
					Attribute<String> attr = ctx.channel().attr(WEBSOCKET_KEY);
					attr.set("");
				}
				
				if (webSocketEvent != null) {
					Map<String, Object> paramter = MapUrlParamsUtil.getUrlParams(paramterStr);
					webSocketEvent.onOpenEvent(baseServer, new WebSocketSession(ctx.channel()), paramter);
				}
			}
		}  else {
			if (httpResource != null) {
				String resFileName = path;
				HashMap<String, Object> map = new HashMap<String, Object>(16);
				map.put("socketurl", WebSocketUtil.getWebSocketLocation(ctx.pipeline(), req, websocketPath));
				ByteBuf res = httpResource.buildWebSocketRes(resFileName, map);
				if (null == res) {
					sendHttpResponse(ctx, req,
							new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
					return;
				} else {
					sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
							res));
				}
			}
			return;
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (frame instanceof TextWebSocketFrame) {
			if (webSocketEvent != null) {
				webSocketEvent.onMessageStringEvent(baseServer, new WebSocketSession(ctx.channel()), ((TextWebSocketFrame) frame).text());
			}
			return;
		}
		
		if (frame instanceof BinaryWebSocketFrame) {
			if (webSocketEvent != null) {
				webSocketEvent.onMessageBinaryEvent(baseServer, new WebSocketSession(ctx.channel()), ((BinaryWebSocketFrame)frame).content());
			}
		}
	}
	
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		int statusCode = res.status().code();
		if (statusCode != HttpResponseStatus.OK.code() && res.content().readableBytes() == 0) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		HttpUtil.setContentLength(res, res.content().readableBytes());

		// Send the response and close the connection if necessary.
		if (!HttpUtil.isKeepAlive(req) || statusCode != HttpResponseStatus.OK.code()) {
			res.headers().set(CONNECTION, CLOSE);
			ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
		} else {
			res.headers().set(CONNECTION, CLOSE);

			///
			//if (req.protocolVersion().equals(HTTP_1_0)) {
			//	res.headers().set(CONNECTION, KEEP_ALIVE);
			//}
			ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
		}
	}

}