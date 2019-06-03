package ext.opensource.netty.client.example.ignite;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@ConfigurationProperties(prefix = "spring.ignite.cache")
public class IgniteProperties {
	/**
	 * 持久化缓存内存初始化大小(MB), 默认值: 64
	 */
	private int persistenceInitialSize = 16;

	/**
	 * 持久化缓存占用内存最大值(MB), 默认值: 128
	 */
	private int persistenceMaxSize = 512;

	/**
	 * 持久化磁盘存储路径
	 */
	private String persistenceStorePath="";

	/**
	 * 非持久化缓存内存初始化大小(MB), 默认值: 64
	 */
	private int notPersistenceInitialSize = 16;

	/**
	 * 非持久化缓存占用内存最大值(MB), 默认值: 128
	 */
	private int notPersistenceMaxSize = 512;

	public int getPersistenceInitialSize() {
		return persistenceInitialSize;
	}

	public IgniteProperties setPersistenceInitialSize(int persistenceInitialSize) {
		this.persistenceInitialSize = persistenceInitialSize;
		return this;
	}

	public int getPersistenceMaxSize() {
		return persistenceMaxSize;
	}

	public IgniteProperties setPersistenceMaxSize(int persistenceMaxSize) {
		this.persistenceMaxSize = persistenceMaxSize;
		return this;
	}

	public String getPersistenceStorePath() {
		return persistenceStorePath;
	}

	public IgniteProperties setPersistenceStorePath(String persistenceStorePath) {
		this.persistenceStorePath = persistenceStorePath;
		return this;
	}

	public int getNotPersistenceInitialSize() {
		return notPersistenceInitialSize;
	}

	public IgniteProperties setNotPersistenceInitialSize(int notPersistenceInitialSize) {
		this.notPersistenceInitialSize = notPersistenceInitialSize;
		return this;
	}

	public int getNotPersistenceMaxSize() {
		return notPersistenceMaxSize;
	}

	public IgniteProperties setNotPersistenceMaxSize(int notPersistenceMaxSize) {
		this.notPersistenceMaxSize = notPersistenceMaxSize;
		return this;
	}
}
