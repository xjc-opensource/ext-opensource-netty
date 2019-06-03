package ext.opensource.netty.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.api.SocketApplication;
import ext.opensource.netty.server.core.BaseServer;
import ext.opensource.netty.server.mqtt.MqttServer;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@SpringBootApplication
public class TestServerApplication implements CommandLineRunner , SocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestServerApplication.class, args);
	}
	
	private BaseServer scoketServer;

	@Override
	public void run(String... strings) {
		scoketServer = new MqttServer();
		scoketServer.bind(8989);
		NettyLog.info("testserver run end "); 
	}

	@Override
	public void shutdown() {
		if (scoketServer != null) {
			scoketServer.shutdown();
		}
	}
}