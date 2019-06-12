package ext.opensource.netty.server.simple;

import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.TranDataProto;
import ext.opensource.netty.common.TranDataProtoUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
	
	@Setter
	private SocketCmdEvent socketCmdEvent;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyLog.debug("msg: {0}", (String) msg);
		messageRecived(ctx, (String) msg);
	}

	private void processMesage(ChannelHandlerContext ctx, TranDataProto reqData) {
		if (reqData.getCode() == null) {
			NettyLog.error("trandata code is null:" + reqData.toString());
			return;
		}

		if (null != socketCmdEvent) {
			TranDataProto reponseData = socketCmdEvent.doCmd(String.valueOf(reqData.getCode()), reqData.getContent());
			if (reponseData != null) {
				int execTimes = 2;
				for (int i = 1; i <= execTimes; i++) {
					TranDataProtoUtil.writeTranData(ctx, reponseData);
					///
					// ctx.writeAndFlush(Unpooled.copiedBuffer(i + " - " + JSON.toJSONString(data),
					// CharsetUtil.UTF_8));
				}
				ctx.flush();
			}
		} else {
			NettyLog.error("error code:" + reqData.getCode());
		}
	}

	private void processPing(ChannelHandlerContext ctx, TranDataProto reqData) {
		TranDataProtoUtil.writeAndFlushTranData(ctx, TranDataProtoUtil.getPongInstance());
	}

	private void messageRecived(ChannelHandlerContext ctx, String str) {
		TranDataProto reqData = TranDataProtoUtil.readTranData(str);
		if (reqData == null) {
			NettyLog.error("reqData is null");
			return;
		}

		if (reqData.getFlag() == TranDataProtoUtil.TranFlag.PING) {
			processPing(ctx, reqData);
		} else if (reqData.getFlag() == TranDataProtoUtil.TranFlag.PONG) {

		} else if (reqData.getFlag() == TranDataProtoUtil.TranFlag.MESSAGE) {
			processMesage(ctx, reqData);
		} else if (reqData.getFlag() == TranDataProtoUtil.TranFlag.MONITER) {

		}
	}
}