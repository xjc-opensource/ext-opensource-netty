package ext.opensource.netty.server.core;

import javax.net.ssl.SSLEngine;

import ext.opensource.netty.common.ChannelManager;
import ext.opensource.netty.common.ChannelManagerHandler;
import ext.opensource.netty.common.HeartbeatServerHandler;
import ext.opensource.netty.common.NettyConstant;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.SocketModel;
import ext.opensource.netty.common.api.BaseChannel;
import ext.opensource.netty.common.exception.SocketRuntimeException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
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
public abstract class BaseServer extends BaseChannel {
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap serverBootstrap;
	
	@Setter @Getter
	private boolean sslClientAuth=false;

	@Getter
	private ChannelManager clientManager = new ChannelManager();

	public BaseServer() {
		this.setHost("0.0.0.0");
	}

	@Override
	protected void init() {
		serverBootstrap = new ServerBootstrap();
	}

	public BaseServer self() {
		return this;
	}

	public boolean useEpoll() {
		String osName = System.getProperty("os.name");
		boolean isLinuxPlatform = osName.toLowerCase().contains("linux");
		return isLinuxPlatform && Epoll.isAvailable();
	}

	private void initGroup() {
		System.out.println("workerCount:" + workerCount);
		if (SocketModel.BLOCK.equals(socketModel)) {
			NettyLog.info("block socket");
			bossGroup = new OioEventLoopGroup(workerCount);
			workerGroup = new OioEventLoopGroup(workerCount);
		} else {
			if (useEpoll()) {
				bossGroup = new EpollEventLoopGroup(workerCount);
				workerGroup = new EpollEventLoopGroup(workerCount);
			} else {
				bossGroup = new NioEventLoopGroup(workerCount);
				workerGroup = new NioEventLoopGroup(workerCount);
			}
		}
	}

	public void bind(String host, int port) {
		this.setHost(host);
		this.bind(port);
	}

	public void bind(int port) {
		this.setPort(port);
		this.bind();
	}

	protected void initBind() {
		
	}

	public void bind() {
		initBind();
		initGroup();
		try {
			serverBootstrap.option(ChannelOption.SO_KEEPALIVE, keepAlive);
			serverBootstrap.option(ChannelOption.TCP_NODELAY, tcpNoDelay);
			serverBootstrap.option(ChannelOption.SO_BACKLOG, soBacklog);
			serverBootstrap.group(bossGroup, workerGroup);
			if (SocketModel.BLOCK.equals(socketModel)) {
				serverBootstrap.channel(OioServerSocketChannel.class);
			} else {
				serverBootstrap.channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
			}
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					if (sslCtx != null) {
				        SSLEngine sslEngine = sslCtx.newEngine(ch.alloc());
				        sslEngine.setUseClientMode(false);
				        sslEngine.setNeedClientAuth(sslClientAuth);
				        ch.pipeline().addFirst(NettyConstant.HANDLER_NAME_SSL, new SslHandler(sslEngine));  
					}
					if (self().isCheckHeartbeat()) {
						NettyLog.info("checkHeartBeat.....");
						ch.pipeline().addLast(
								new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));
						ch.pipeline().addLast(NettyConstant.HANDLER_NAME_HEARTCHECK, new HeartbeatServerHandler());
					}

					ch.pipeline().addLast(new ChannelManagerHandler(self().getClientManager()));

					initSocketChannel(ch);
				}
			});

			ChannelFuture future = serverBootstrap.bind(this.getHost(), this.getPort());
			channel = future.channel();

			future.sync();

		} catch (Exception ex) {
			NettyLog.error("Netty start error:", ex);
			throw new SocketRuntimeException(ex);
		} finally {
			if (channel != null && channel.isActive()) {
				NettyLog.info("Netty server listening " + this.getHost() + " on port " + this.getPort()
						+ " and ready for connections...");
			} else {
				NettyLog.error("Netty server start up Error!");
			}
		}
	}

	protected void initSocketChannel(SocketChannel ch) {
	}

	@Override
	public void shutdown() {
		NettyLog.info("Shutdown Netty Server...");
		if (channel != null) {
			channel.close().syncUninterruptibly();
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully().syncUninterruptibly();
		}
		if (bossGroup != null) {
			bossGroup.shutdownGracefully().syncUninterruptibly();
		}
		NettyLog.info("Shutdown Netty Server Success!");
	}

	protected void broadcastMessage(Object msg) {
		clientManager.broadcastMessage(msg);
	}

	/**
	 * broadcast message
	 * @param msg
	 */
	public abstract void broadcastMessageString(String msg);
}