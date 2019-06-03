package ext.opensource.netty.server.httpsocket;

import ext.opensource.netty.server.core.BaseServer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class HttpServer extends BaseServer {
	@Getter @Setter
	private BaseHttpResource httpResource;
	
	@Override
	public void broadcastMessageString(String msg) {
		// TODO Auto-generated method stub
	}
	

	
	@Override
	protected void initSocketChannel(SocketChannel ch) {
		super.initSocketChannel(ch);
		ch.pipeline().addLast(new HttpServerCodec());
		ch.pipeline().addLast(new HttpObjectAggregator(65536));
		ch.pipeline().addLast(new ChunkedWriteHandler());
		
		HttpServerHandler httpServerHandler = new HttpServerHandler(this, httpResource);
		processHttpHandler(httpServerHandler);
		
		ch.pipeline().addLast("http", httpServerHandler);
	}
	

	protected void processHttpHandler(HttpServerHandler httpServerHandler) {
		
	}
}
