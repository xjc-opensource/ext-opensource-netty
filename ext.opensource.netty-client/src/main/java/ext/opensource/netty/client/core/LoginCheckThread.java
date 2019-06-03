package ext.opensource.netty.client.core;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class LoginCheckThread extends Thread {
	private BaseClient client;
	private long timeOutMillis;
	private long startTime;
	public LoginCheckThread(BaseClient client, long startTime, long timeOutMillis) {
		this.client = client;
		this.timeOutMillis = timeOutMillis;
		this.startTime = startTime;
	}
	
	@Override
	public void run() {
		check();
	}
	
	public void check() {
		boolean bTimeOut = false;
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
			
			if (timeOutMillis > 0) {
				if ((System.currentTimeMillis() - startTime) > timeOutMillis) {
					bTimeOut = true;
				}
			}

			if ((this.client== null) || (this.client.checkConnectFlag(bTimeOut, System.currentTimeMillis() - startTime))) {
				break;
			}
			if (bTimeOut) {
				break;
			}
		}
	}
}
