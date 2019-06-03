package ext.opensource.netty.client.httpsocket;

import ext.opensource.netty.common.NettyLog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
	private WebSocketClientHandshaker handshaker;
	///private ChannelPromise handshakeFuture;
	
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) {
       /// this.handshakeFuture = ctx.newPromise();
    }
	
	/*public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }*/

	public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
		this.handshaker = handshaker;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyLog.info(String.format("Active id:%s", ctx.channel().id().asLongText()));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyLog.info(String.format("Inactive id:%s", ctx.channel().id().asLongText()));
		super.channelInactive(ctx);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		NettyLog.info(String.format("exceptionCaught id:%s", ctx.channel().id().asLongText()));
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpResponse) {
			handleHttpResponse(ctx, (FullHttpResponse) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		} else {
		}
	}

	private void handleHttpResponse(ChannelHandlerContext ctx, FullHttpResponse response) {
		if (!this.handshaker.isHandshakeComplete()) {
			try {
				this.handshaker.finishHandshake(ctx.channel(), response);
				///设置成功
				//this.handshakeFuture.setSuccess();
				System.out.println("WebSocket Client connected! response headers[sec-websocket-extensions]:{}"
						+ response.headers());
			} catch (WebSocketHandshakeException var7) {
				String errorMsg = String.format("WebSocket Client failed to connect,status:%s,reason:%s",
						response.status(), response.content().toString(CharsetUtil.UTF_8));
				NettyLog.error(errorMsg, var7);
				///this.handshakeFuture.setFailure(new Exception(errorMsg));
			}
		} else {
			throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status()
					+ ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof TextWebSocketFrame) {
			TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
			System.out.println("TextWebSocketFrame:" + textFrame.text());
		} else if (frame instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame) frame;
			System.out.println("BinaryWebSocketFrame:" + binFrame.toString());
		} else if (frame instanceof PongWebSocketFrame) {
			System.out.println("WebSocket Client received pong");
		} else if (frame instanceof CloseWebSocketFrame) {
			System.out.println("receive close frame");
			ctx.channel().close();
		}
	}
}