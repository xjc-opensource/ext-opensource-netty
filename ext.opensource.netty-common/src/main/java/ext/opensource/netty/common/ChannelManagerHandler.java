package ext.opensource.netty.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ChannelManagerHandler extends ChannelInboundHandlerAdapter {
	protected ChannelManager clientManager;

	protected ChannelManagerHandler() {
	}

	public ChannelManagerHandler(ChannelManager clientManager) {
		this.clientManager = clientManager;
		if (null == this.clientManager) {
			throw new RuntimeException("clientManager is null");
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		
		///System.out.println("channelActivexx: " + ctx.channel().id().asShortText());
		
		if (this.clientManager != null) {
			clientManager.addChannel(ctx.channel());
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		
		///System.out.println("channelInactive: " + ctx.channel().id().asShortText());
		
		if (this.clientManager != null) {
			clientManager.removeChannel(ctx.channel());
		}

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (this.clientManager != null) {
			clientManager.addReceiveCount();
		}
		super.channelRead(ctx, msg);
	}
}
