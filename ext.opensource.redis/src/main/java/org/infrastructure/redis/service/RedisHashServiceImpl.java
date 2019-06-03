package org.infrastructure.redis.service;

import javax.annotation.Resource;

import org.infrastructure.redis.IRedisHashService;
import org.infrastructure.redis.core.GsonRedisSerializer;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Service
@Scope("prototype")
@SuppressWarnings("unchecked")
public class RedisHashServiceImpl <V> implements IRedisHashService<V> {
	private RedisTemplate<String, V> redisTemplate;
	private RedisSerializer<String> hkeySerializer;
	private RedisSerializer<V> hvalueSerializer;
	private RedisSerializer<String> serializer;

	@Resource(name="redisTemplateHash")
	private void setRedisTemplate(RedisTemplate<String, V> redisTemplateHash) {
		this.redisTemplate = redisTemplateHash;
		hkeySerializer = (RedisSerializer<String>) redisTemplate.getStringSerializer();
		hvalueSerializer = (RedisSerializer<V>) redisTemplate.getHashValueSerializer();
		serializer = redisTemplate.getStringSerializer();
		//System.err.println(hkeySerializer.getClass().getSimpleName());
	}
	
	@Override
	public void setValueClass(Class<V> clz) {
		hvalueSerializer = new GsonRedisSerializer<V>(clz);
	}

	@Override
	public Boolean hSet(String key, String field, V obj) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) {
				return connection.hSet(serializer.serialize(key), hkeySerializer.serialize(field),
						hvalueSerializer.serialize(obj));
			}
		});
	}

	@Override
	public V hGet(String key, String field) {
		return  redisTemplate.execute(new RedisCallback<V>() {
			@Override
			public V doInRedis(RedisConnection connection) {
				byte[] res = connection.hGet(serializer.serialize(key), hkeySerializer.serialize(field));
				if (res != null && res.length > 0) {
					return hvalueSerializer.deserialize(res);
				} else {
					return null;
				}
			}
		});
	}

	@Override
	public void hDel(final String key, final String field) {
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.hDel(serializer.serialize(key), hkeySerializer.serialize(field));
			}
		});
	}
	
	@Override
	public boolean hExists(final String key, final String field) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) {
				return connection.hExists(serializer.serialize(key), hkeySerializer.serialize(field));
			}
		});
	}
	
	@Override
	 public long hLen(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.hLen(serializer.serialize(key));
			}
		});
	}
    
    @Override
    public long hIncrBy(final String key, final String field, final long number) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
            	return connection.hIncrBy(serializer.serialize(key), hkeySerializer.serialize(field), number);
            }
        });
        return result == null? 0 : result;
    }
    
    @Override
    public long incr(final String key) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.incr(serializer.serialize(key));
            }
        });
        return result == null? 0 : result;
    }
    
    @Override
    public boolean delKey(final String key) {
    	Long result = redisTemplate.execute(new RedisCallback<Long>() {
             @Override
             public Long doInRedis(RedisConnection connection) throws DataAccessException {
             	 return connection.del(serializer.serialize(key));
             }
         });
         return result > 0;
    }
 
}
