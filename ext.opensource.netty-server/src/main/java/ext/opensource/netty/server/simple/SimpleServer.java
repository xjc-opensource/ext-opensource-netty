package ext.opensource.netty.server.simple;

import java.nio.charset.Charset;

import ext.opensource.netty.common.LogDispatchHandler;
import ext.opensource.netty.common.TranDataProtoUtil;
import ext.opensource.netty.server.core.BaseServer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class SimpleServer extends BaseServer {
	@Override
    protected void initSocketChannel(SocketChannel ch) {
		super.initSocketChannel(ch);
		ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
		ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
		ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
		ch.pipeline().addLast(new LogDispatchHandler());
		ch.pipeline().addLast(new SimpleServerHandler());
    }
	
	@Override
	public void broadcastMessageString(String msg) {
		super.broadcastMessage(TranDataProtoUtil.getMsgSocketData(TranDataProtoUtil.getMsgInstance(1003, "test")));
	}

	
}