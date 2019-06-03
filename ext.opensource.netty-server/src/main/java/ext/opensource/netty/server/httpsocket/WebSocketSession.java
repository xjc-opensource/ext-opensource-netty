package ext.opensource.netty.server.httpsocket;

import java.nio.ByteBuffer;

import ext.opensource.netty.common.BaseSocketSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class WebSocketSession extends BaseSocketSession {
	private static final long serialVersionUID = 1L;

	public WebSocketSession(Channel channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ChannelFuture sendTextMessage(String message) {
        return channel().writeAndFlush(new TextWebSocketFrame(message));
    }

    public ChannelFuture sendTextMessage(ByteBuf byteBuf) {
        return channel().writeAndFlush(new TextWebSocketFrame(byteBuf));
    }

    public ChannelFuture sendTextMessage(ByteBuffer byteBuffer) {
        ByteBuf buffer = channel().alloc().buffer(byteBuffer.remaining());
        buffer.writeBytes(byteBuffer);
        return channel().writeAndFlush(new TextWebSocketFrame(buffer));
    }

    public ChannelFuture sendBinaryMessage(byte[] bytes) {
        ByteBuf buffer = channel().alloc().buffer(bytes.length);
        return channel().writeAndFlush(new BinaryWebSocketFrame(buffer.writeBytes(bytes)));
    }
    @Override
    public ChannelFuture sendBinaryMessage(ByteBuf byteBuf) {
        return channel().writeAndFlush(new BinaryWebSocketFrame(byteBuf));
    }

    public ChannelFuture sendBinaryMessage(ByteBuffer byteBuffer) {
        ByteBuf buffer = channel().alloc().buffer(byteBuffer.remaining());
        buffer.writeBytes(byteBuffer);
        return channel().writeAndFlush(new BinaryWebSocketFrame(buffer));
    }
}
