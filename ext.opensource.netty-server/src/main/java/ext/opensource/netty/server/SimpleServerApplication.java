package ext.opensource.netty.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.api.SocketApplication;
import ext.opensource.netty.server.simple.SimpleServer;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

//@SpringBootApplication

public class SimpleServerApplication implements CommandLineRunner, SocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(SimpleServerApplication.class, args);
	}
	private SimpleServer nettyServer;

	@Override
	public void run(String... strings) {
		nettyServer = new SimpleServer();
		///nettyServer.setSocketModel(SocketModel.BLOCK);
		nettyServer.setCheckHeartbeat(false);
		nettyServer.setAllIdleTimeSeconds(90); 
		nettyServer.bind(8989);  
		
		NettyLog.info("simple server run end ");
	}

	@Override
	public void shutdown() {
		if (nettyServer != null) {
			nettyServer.shutdown(); 
		}
	}
}