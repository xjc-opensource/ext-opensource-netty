package ext.opensource.netty.client.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngine;

import ext.opensource.netty.client.mqtt.common.ClientEvent;
import ext.opensource.netty.common.HeartbeatClientHandler;
import ext.opensource.netty.common.NettyConstant;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.NettyUtil;
import ext.opensource.netty.common.SocketModel;
import ext.opensource.netty.common.api.BaseChannel;
import ext.opensource.netty.common.exception.LoginException;
import ext.opensource.netty.common.exception.SocketRuntimeException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@SuppressWarnings("deprecation")
public abstract class BaseClient extends BaseChannel implements ClientProcess {
	private Semaphore semConnect = new Semaphore(0);
	private Bootstrap bootstrap;
	private EventLoopGroup group;

	private ScheduledExecutorService reConnectService;

	@Setter
	private int connectTimeOutMillis = 1000 * 20;

	@Setter
	@Getter
	private boolean syncConnect = false;
	@Setter
	private boolean checkConnectFlag = false;
	@Setter
	private int checkConnectSeconds = 10;

	private int loginFlag = 0;
	
	@Setter
	private ClientEvent loginSuccess;

	public BaseClient() {
		this.setHost("127.0.0.1");
	}

	public BaseClient self() {
		return this;
	}

	protected void initConnect() {
		if (SocketModel.BLOCK.equals(socketModel)) {
			group = new OioEventLoopGroup();
		} else {
			group = new NioEventLoopGroup();
		}
		bootstrap = new Bootstrap();
	}

	public void connect(String host, int port) {
		this.setHost(host);
		this.connect(port);
	}

	public void connect(int port) {
		this.setPort(port);
		this.connect();
	}

	public void connect() {
		initConnect();
		bootstrap.option(ChannelOption.SO_KEEPALIVE, keepAlive);
		bootstrap.option(ChannelOption.TCP_NODELAY, tcpNoDelay);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeOutMillis);
		if (SocketModel.BLOCK.equals(socketModel)) {
			NettyLog.info("block socket");
			bootstrap.group(group).channel(OioSocketChannel.class);
		} else {
			bootstrap.group(group).channel(NioSocketChannel.class);
		}
		bootstrap.remoteAddress(this.getHost(), this.getPort());

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				initSocketChannel(ch);
				
				if (sslCtx != null) {
			        SSLEngine sslEngine = sslCtx.newEngine(ch.alloc());
			        sslEngine.setUseClientMode(true);
			        ch.pipeline().addFirst(NettyConstant.HANDLER_NAME_SSL, new SslHandler(sslEngine));  
				}

				if (self().isCheckHeartbeat()) {
					NettyLog.info("checkHeartBeat.....");
					ch.pipeline().addLast(
							new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));
					ch.pipeline().addLast(NettyConstant.HANDLER_NAME_HEARTCHECK, new HeartbeatClientHandler());
				}
			}
		});
		NettyLog.debug("connect start");
		doConnect();

		if (checkConnectFlag && checkConnectSeconds > 1) {
			if (reConnectService == null) {
				reConnectService = Executors.newSingleThreadScheduledExecutor();
				
				// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
				reConnectService.scheduleWithFixedDelay(new ConnectCheckClient(this), 20, checkConnectSeconds,
						TimeUnit.SECONDS);
			}  
		}
	}

	protected void initSocketChannel(SocketChannel ch) {
	}

	public void reConnect() {
		if (!isOpen() || !isActive()) {
			NettyLog.info("reConnect");
			doConnect();
		}
	}

	/**
	 * 发送信息
	 * @param msg
	 */
	public abstract void sendMessage(String msg);

	public boolean checkConnectFlag(boolean bTimeOut, long timeOutMills) {

		if ((loginFlag == 0) && bTimeOut) {
			NettyLog.error("login timeout: " + timeOutMills);

			if ((channel != null) && channel.isActive()) {
				channel.close();
			}
		}
		return loginFlag > 0;
	}

	private void doConnect() {
		try {
			long connectStartTime = System.currentTimeMillis();

			ChannelFuture future = bootstrap.connect();
			channel = future.channel();
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture ch) {
					try {
						finishConnectEvent(ch);
					} finally {
						semConnect.release();
					}
				}
			});

			LoginCheckThread loginCheckObj = new LoginCheckThread(this, connectStartTime, connectTimeOutMillis);
			if (syncConnect) {
				semConnect.acquire();
				loginCheckObj.check();
			} else {
				loginCheckObj.start();
			}

		} catch (Exception ex) {
			NettyLog.error("Netty start error:", ex);
			throw new SocketRuntimeException(ex);
		} finally {
		}
	}

	protected void finishConnectEvent(ChannelFuture ch) {
		if (ch.isSuccess() && (channel != null) && channel.isActive()) {
			NettyLog.info("client connect " + channel.remoteAddress());
			prepareLogin();
		} else {
			NettyLog.info(" client connect failure!");
		}
	}

	@Override
	public void shutdown() {
		if (reConnectService != null) {
			reConnectService.shutdown();
			reConnectService = null;
		}

		if (channel != null) {
			if (this.isActive()) {
				this.disConnect();
			}
			NettyLog.info("Shutdown Netty Client...");
			channel.close().syncUninterruptibly();
			channel = null;
		}
		group.shutdownGracefully().syncUninterruptibly();
		NettyLog.info("Shutdown Netty Client Success!");
	}

	private void prepareLogin() {
		loginFlag = 0;
		if (loginInit()) {
			NettyLog.debug("login start...");
		} else {
			loginFlag = 1;
		}
	}

	protected boolean loginInit() {
		return false;
	}
	
	public String getClientId() {
		return "";
	}

	@Override
	public void loginFinish(boolean bResult, LoginException exception) {
		NettyLog.debug("login finish");
		if (bResult) {
			NettyUtil.setClientId(channel, getClientId());
			loginFlag = 1;
			if (loginSuccess != null) {
				loginSuccess.process();
			}
		} else {
			loginFlag = 2;
			if (exception != null) {
				NettyLog.error("login error", exception);
			}
		}
	}

	@Override
	public void disConnect() {
		if (channel != null) {
			channel.close();
		}
	}
}
