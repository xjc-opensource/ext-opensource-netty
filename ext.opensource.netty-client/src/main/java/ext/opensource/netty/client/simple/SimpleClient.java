package ext.opensource.netty.client.simple;

import java.nio.charset.Charset;

import ext.opensource.netty.client.core.BaseClient;
import ext.opensource.netty.common.LogDispatchHandler;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.TranDataProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class SimpleClient extends BaseClient {
	@Override
	protected void initSocketChannel(SocketChannel ch) {
		super.initSocketChannel(ch);
		ch.pipeline().addLast(new LineBasedFrameDecoder(1024)); 
		ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
		ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
		///ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));

		ch.pipeline().addLast(new LogDispatchHandler());
		ch.pipeline().addLast(new ClientSimpleHandler());
		///ch.pipeline().addLast(new ClientSimpleHandlerX());
	}
	
	@Override
	public void sendMessage(String msg) {
		TranDataProtoUtil.writeAndFlushTranData(this.channel, TranDataProtoUtil.getMsgInstance(10003, msg));
	}
	
	public class ClientSimpleHandler extends ChannelInboundHandlerAdapter {
		
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {		
			int execTimes = 2;
			for (int i=0; i<execTimes; i++) {
				TranDataProtoUtil.writeAndFlushTranData(ctx, TranDataProtoUtil.getMsgInstance(10003, String.format(" %d -Hello Netty-Server!", i)));
				///
				//ctx.writeAndFlush(Unpooled.copiedBuffer(JSON.toJSONString(data) + "\r\n", CharsetUtil.UTF_8));
			}
			ctx.flush();
			super.channelActive(ctx);
		}
		
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			NettyLog.info("客户端接收到消息: " + msg); 
			super.channelRead(ctx, msg);
		}
		
	}
	
	public class ClientSimpleHandlerX extends SimpleChannelInboundHandler<String> {
		private ClientSimpleHandlerX() {
			super(true);
		}
		
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			NettyLog.info("客户端接收到消息XX: " + msg); 
		}

	}
	
}
