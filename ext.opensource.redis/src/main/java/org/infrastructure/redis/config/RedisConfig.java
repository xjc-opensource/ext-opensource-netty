package org.infrastructure.redis.config;


import org.infrastructure.redis.core.GsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@ComponentScan(basePackages = "org.infrastructure.redis.service")
@EnableConfigurationProperties(RedisProperties.class) 
@Configuration
public class RedisConfig {
	@Autowired
	private RedisProperties properties;

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(properties.getPool().getMaxActive());
		poolConfig.setMaxIdle(properties.getPool().getMaxIdle());
		poolConfig.setMaxWaitMillis(properties.getPool().getMaxWait());
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnCreate(true);
		poolConfig.setTestWhileIdle(true);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
		jedisConnectionFactory.setHostName(properties.getHost());
		if ((properties.getPassword() != null) && (properties.getPassword().trim().length() > 0)) {
			jedisConnectionFactory.setPassword(properties.getPassword());
		}
		jedisConnectionFactory.setDatabase(properties.getDatabase());
		jedisConnectionFactory.setPort(properties.getPort());
		jedisConnectionFactory.setTimeout(properties.getTimeout());

		// 其他配置，可再次扩展

		return jedisConnectionFactory;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean(name="redisTemplate")    
    public RedisTemplate redisTemplate(){  
        RedisTemplate redisTemplate = new RedisTemplate();    
 
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());  
        redisTemplate.setValueSerializer(new StringRedisSerializer());  
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());  
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());  
        redisTemplate.afterPropertiesSet();  
        redisTemplate.setEnableTransactionSupport(true);  
          
        return redisTemplate;    
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean(name="redisTemplateHash")  
	@Scope("prototype")
    public RedisTemplate redisTemplateHash(){  
		RedisSerializer valueSerializer = new GsonRedisSerializer(Object.class);
		RedisSerializer keySerializer = new StringRedisSerializer();

        RedisTemplate redisTemplate = new RedisTemplate();    
         
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());  
        redisTemplate.setValueSerializer(new StringRedisSerializer());  
        redisTemplate.setHashKeySerializer(keySerializer);  
        redisTemplate.setHashValueSerializer(valueSerializer);  
        redisTemplate.afterPropertiesSet();  
        redisTemplate.setEnableTransactionSupport(true);  
          
        return redisTemplate;    
    }
}