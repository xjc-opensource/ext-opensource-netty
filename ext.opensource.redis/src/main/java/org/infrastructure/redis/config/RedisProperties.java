package org.infrastructure.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
	private int database;
	private String host;
	private String password;
	private int port;
	private int timeout = 10000;

	private RedisProperties.Pool pool = new Pool();

	public RedisProperties() {
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RedisProperties.Pool getPool() {
		return this.pool;
	}

	public void setPool(RedisProperties.Pool pool) {
		this.pool = pool;
	}

	public int getDatabase() {
		return this.database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public static class Pool {
		private int maxIdle = 8;
		private int minIdle = 0;
		private int maxActive = 8;
		private int maxWait = -1;

		public Pool() {
		}

		public int getMaxIdle() {
			return this.maxIdle;
		}

		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}

		public int getMinIdle() {
			return this.minIdle;
		}

		public void setMinIdle(int minIdle) {
			this.minIdle = minIdle;
		}

		public int getMaxActive() {
			return this.maxActive;
		}

		public void setMaxActive(int maxActive) {
			this.maxActive = maxActive;
		}

		public int getMaxWait() {
			return this.maxWait;
		}

		public void setMaxWait(int maxWait) {
			this.maxWait = maxWait;
		}

	}
}