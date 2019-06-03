package ext.opensource.netty.common;

import ext.opensource.netty.common.NettyLog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class LogDispatchHandler extends ChannelInboundHandlerAdapter {
    
	@Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		NettyLog.info(String.format("Registered id:%s", ctx.channel().id().asLongText()));
    	super.channelRegistered(ctx);
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    	NettyLog.info(String.format("UnRegistered id:%s", ctx.channel().id().asLongText()));
    	super.channelUnregistered(ctx);
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
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyLog.info(String.format("Read id:%s", ctx.channel().id().asLongText()));
		super.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		NettyLog.info(String.format("exceptionCaught id:%s", ctx.channel().id().asLongText()));
		super.exceptionCaught(ctx, cause);
	}
}
