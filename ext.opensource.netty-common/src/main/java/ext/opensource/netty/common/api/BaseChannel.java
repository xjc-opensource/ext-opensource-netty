package ext.opensource.netty.common.api;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.Semaphore;

import ext.opensource.netty.common.SocketModel;

import io.netty.channel.Channel;
import io.netty.handler.ssl.SslContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public abstract class BaseChannel {
	private Semaphore blockExec = new Semaphore(0);
	
	@Getter
	protected Channel channel;
	
	/**
	 * 主机名
	 */
	@Getter @Setter
	private String host;
	
	/**
	 * 端口号
	 */
	@Getter @Setter
	private int port=8181;
	
    /**
     * 工作线程池大小
     */
	@Getter @Setter
    protected int workerCount;
	
    /**
     * 是否启用keepAlive
     */
	@Getter @Setter
    protected boolean keepAlive = false;
	
    /**
     * 是否启用tcpNoDelay
     */
	@Getter @Setter
    protected boolean tcpNoDelay = false;
	
	/**
	 * Sokcet参数, 存放已完成三次握手请求的队列最大长度, 默认511长度
	 */
	@Setter @Getter
	protected int soBacklog = 1024;
    
    
    /**
     * 设置是否心跳检查
     */
	@Getter @Setter
    protected boolean checkHeartbeat = false;
	
    /**
     * 心跳检查时的读空闲时间
     */
	@Getter @Setter
    protected int readerIdleTimeSeconds = 0;
	
    /**
     * 心跳检查时的写空闲时间
     */
	@Getter @Setter
    protected int writerIdleTimeSeconds = 0;
	
    /**
     * 心跳检查时的读写空闲时间
     */
	@Getter @Setter
    protected int allIdleTimeSeconds = 90;

    /**
     *编码格式
     */
	@Getter @Setter
	@NonNull
	protected String charsetName = "utf-8";
	
	
	/**
	 * ssl
	 */
	@Setter @Getter
	protected SslContext sslCtx = null;

	
	/**
	 * socketModel
	 */
	@Setter @Getter
	@NonNull
	protected SocketModel socketModel = SocketModel.UNBOLOCK;
	
    
    protected List<EventListener> eventListeners = new ArrayList<>();
    
	public BaseChannel() {
	   // 默认工作线程数
       this.workerCount = Runtime.getRuntime().availableProcessors() * 2;
       //添加JVM关闭时的勾子
       Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
       
       this.init();
	}
	
	public boolean isActive() {
		return (channel == null)? false : channel.isActive();
	}
	
	public boolean isOpen() {
		return (channel == null)? false : channel.isOpen();
	}
	
	protected void init() {
		
	}
	
	/**
	 * close socket
	 */
	public abstract void shutdown();
	
    public void addEventListener(EventListener listener) {
        this.eventListeners.add(listener);
    }
    
    public void requireSync() {
    	try {
			blockExec.acquire();
		} catch (InterruptedException e) {
			blockExec.release();
			e.printStackTrace();
		}
    }
    
    public void releaseSync() {
    	blockExec.release(0);
    }

	class ShutdownHook extends Thread {
        private BaseChannel baseChannel;

        public ShutdownHook(BaseChannel baseChannel) {
            this.baseChannel = baseChannel;
        }

        @Override
        public void run() {
        	releaseSync();
        	baseChannel.shutdown();
        }
    }
}
