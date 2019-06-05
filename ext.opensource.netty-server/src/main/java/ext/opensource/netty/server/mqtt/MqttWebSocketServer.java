
package ext.opensource.netty.server.mqtt;

import ext.opensource.netty.server.httpsocket.BaseHttpResource;
import ext.opensource.netty.server.httpsocket.HttpResourceHander;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttWebSocketServer extends MqttServer {
	@Getter @Setter
	private BaseHttpResource httpResource;
	@Setter 
	private String websocketPath;
	
	@Override
	protected void initSocketChannel(SocketChannel ch) {
		super.initSocketChannel(ch);
		ch.pipeline().addBefore(HANDLER_MQTTDECODER, "HttpServerCodec", new HttpServerCodec());
		ch.pipeline().addBefore(HANDLER_MQTTDECODER, "HttpObjectAggregator", new HttpObjectAggregator(65536));
		ch.pipeline().addBefore(HANDLER_MQTTDECODER, "ChunkedWriteHandler", new ChunkedWriteHandler());
		ch.pipeline().addBefore(HANDLER_MQTTDECODER, "compressor ", new HttpContentCompressor());
		ch.pipeline().addBefore(HANDLER_MQTTDECODER, "protocol", new WebSocketServerProtocolHandler(websocketPath, "mqtt,mqttv3.1,mqttv3.1.1", true, 65536));
		ch.pipeline().addBefore(HANDLER_MQTTDECODER, "mqttWebSocket", new MqttWebSocketCodec());

		HttpResourceHander httpResourceHander = new HttpResourceHander(httpResource);
		ch.pipeline().addLast("httpResource", httpResourceHander);
	}
}
