package ext.opensource.netty.client.example;

import java.net.URISyntaxException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import ext.opensource.netty.client.simple.SimpleClient;
import ext.opensource.netty.common.api.SocketApplication;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

//@SpringBootApplication

public class SimpleClientApplication implements CommandLineRunner, SocketApplication {
	private SimpleClient nettyClient;

	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}

	@Override
	public void run(String... strings) throws URISyntaxException {

		nettyClient = new SimpleClient();
		///
		// nettyClient.setSocketModel(SocketModel.BLOCK);
		nettyClient.setCheckHeartbeat(false);
		nettyClient.setReaderIdleTimeSeconds(10);
		nettyClient.setCheckConnectFlag(true);
		nettyClient.connect(8989);

		System.err.println("simple client run end");
	}

	@Override
	public void shutdown() {
		if (nettyClient != null) {
			nettyClient.shutdown();
			nettyClient = null;
		}
	}
}
