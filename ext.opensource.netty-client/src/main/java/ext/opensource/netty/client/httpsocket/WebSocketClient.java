package ext.opensource.netty.client.httpsocket;

import java.net.URI;
import java.net.URISyntaxException;

import ext.opensource.netty.client.core.BaseClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslHandler;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class WebSocketClient extends BaseClient {
	private String websocketPath;
	private WebSocketClientHandshaker handshaker;

	public WebSocketClient(String websocketPath) {
		this.setSyncConnect(true);
		this.websocketPath = websocketPath;
	}

	@Override
	public void sendMessage(String msg) {
		this.channel.writeAndFlush(new TextWebSocketFrame(msg));
	}

	@Override
	protected void initSocketChannel(SocketChannel ch) {
		super.initSocketChannel(ch);

		URI websocketURI = null;
		try {
			websocketURI = new URI(getWebSocketUrl(ch.pipeline(), websocketPath));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		this.handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13,
				(String) null, true, new DefaultHttpHeaders(), 5 * 1024 * 1024);

		ch.pipeline().addLast(new HttpClientCodec());
		ch.pipeline().addLast(new HttpObjectAggregator(65535));
		ch.pipeline().addLast(new WebSocketClientHandler(handshaker));
	}

	@Override
	protected void finishConnectEvent(ChannelFuture ch) {
		super.finishConnectEvent(ch);
		if (ch.isSuccess()) {
			// start handskake
			ChannelFuture handFuture = this.handshaker.handshake(this.getChannel());
			if (this.isSyncConnect()) {
				handFuture.syncUninterruptibly();
			}
		}
	}

	private String getWebSocketUrl(ChannelPipeline cp, String path) {
		String protocol = "ws";
		if (cp.get(SslHandler.class) != null) {
			// SSL in use so use Secure WebSockets
			protocol = "wss";
		}
		return protocol + "://" + this.getHost() + path;
	}

}
