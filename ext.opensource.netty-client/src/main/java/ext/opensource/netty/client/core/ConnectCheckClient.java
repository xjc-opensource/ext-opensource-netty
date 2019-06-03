package ext.opensource.netty.client.core;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ConnectCheckClient implements Runnable {
	private long count;
	private BaseClient client = null;

	public ConnectCheckClient(BaseClient client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		count ++; 
		if (count >= Long.MAX_VALUE) {
			count= 0;
		}
		if (client != null) {
			client.reConnect();
		}
	}
}
