package ext.opensource.netty.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCountUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ChannelManager {
	/// ChannelGroup netty
	// private static final ChannelGroup channelsx = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	private ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
	private AtomicLong receiveCount = new AtomicLong(0);
	private AtomicLong sendCount = new AtomicLong(0);
	private AtomicLong heartbeatCount = new AtomicLong(0);

	public void addChannel(Channel channel) {
		if (null != channel) {
			channels.putIfAbsent(channel.id().asLongText(), channel);
		}
	}

	public void removeChannel(Channel channel) {
		if (null != channel) {
			channels.remove(channel.id().asLongText());
		}
	}

	public long clientCount() {
		return channels.mappingCount();
	}

	public long addReceiveCount() {
		return receiveCount.incrementAndGet();
	}

	public long receiveCount() {
		return receiveCount.get();
	}

	public long addSendCount() {
		return sendCount.incrementAndGet();
	}

	public long sendCount() {
		return sendCount.get();
	}

	public long heartbeatCount() {
		return heartbeatCount.get();
	}
	
    private static Object safeDuplicate(Object message) {
        if (message instanceof ByteBuf) {
            return ((ByteBuf) message).retainedDuplicate();
        } else if (message instanceof ByteBufHolder) {
            return ((ByteBufHolder) message).retainedDuplicate();
        } else {
            return ReferenceCountUtil.retain(message);
        }
    }
	
	public void broadcastMessage(Object msg) {
		System.out.println("broad count:" + clientCount());
		for (Channel ch : channels.values()) {
			if (ch != null && ch.isActive()) {
				ch.writeAndFlush(safeDuplicate(msg));
			}
		}
	}
}
