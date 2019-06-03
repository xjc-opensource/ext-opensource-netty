package ext.opensource.netty.common;

import com.alibaba.fastjson.JSON;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class TranDataProtoUtil {
	public interface TranFlag {
		public static final int PING = 1;
		public static final int PONG = 2;
		public static final int MESSAGE = 3;
		public static final int MONITER = 4;
	}

	public static TranDataProto getPingInstance() {
		TranDataProto data = new TranDataProto();
		data.setFlag(TranFlag.PING);
		return data;
	}

	public static TranDataProto getPongInstance() {
		TranDataProto data = new TranDataProto();
		data.setFlag(TranFlag.PONG);
		return data;
	}

	public static TranDataProto getMsgInstance(int code, String content) {
		TranDataProto data = new TranDataProto();
		data.setFlag(TranFlag.MESSAGE);
		data.setCode(code);
		data.setContent(content);
		return data;
	}
	
	public static Object getMsgSocketData(TranDataProto data) {
		return JSON.toJSONString(data) + "\r\n";
	}

	public static void writeTranData(ChannelHandlerContext ctx, TranDataProto data) {
		ctx.write(getMsgSocketData(data));
	}

	public static void writeAndFlushTranData(ChannelHandlerContext ctx, TranDataProto data) {
		writeTranData(ctx, data);
		ctx.flush();
	}
	
	public static void writeTranData(Channel ch, TranDataProto data) {
		ch.write(getMsgSocketData(data));
	}

	public static void writeAndFlushTranData(Channel ch, TranDataProto data) {
		writeTranData(ch, data);
		ch.flush();
	}

	public static TranDataProto readTranData(String str) {
		TranDataProto reqData = null;
		try {
			reqData = JSON.parseObject(str, TranDataProto.class);
		} catch (Exception ex) {
			NettyLog.error("error josn: " + str);
		}
		return reqData;
	}

}
