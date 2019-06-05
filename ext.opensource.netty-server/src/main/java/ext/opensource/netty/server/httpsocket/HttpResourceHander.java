
package ext.opensource.netty.server.httpsocket;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class HttpResourceHander
		extends
			SimpleChannelInboundHandler<FullHttpRequest> {
	private BaseHttpResource httpResource;

	public HttpResourceHander(BaseHttpResource httpResource) {
		this.httpResource = httpResource;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req)
			throws Exception {

		String uri = req.uri();
		int index = uri.indexOf("?");
		String path = null;
		if (index == -1) {
			path = uri;
		} else {
			path = uri.substring(0, index);
		}
		
		if (httpResource != null) {
			String resFileName = path;
			
			ByteBuf res = httpResource.buildWebSocketRes(req, resFileName);
			if (null == res) {
				sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
						HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
				return;
			} else {
				sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
						HttpVersion.HTTP_1_1, HttpResponseStatus.OK, res));
			}
		}
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		int statusCode = res.status().code();
		if (statusCode != HttpResponseStatus.OK.code()
				&& res.content().readableBytes() == 0) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
					CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		HttpUtil.setContentLength(res, res.content().readableBytes());

		// Send the response and close the connection if necessary.
		if (!HttpUtil.isKeepAlive(req)
				|| statusCode != HttpResponseStatus.OK.code()) {
			res.headers().set(CONNECTION, CLOSE);
			ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
		} else {
			res.headers().set(CONNECTION, CLOSE);
			ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
