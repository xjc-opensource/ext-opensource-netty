package org.infrastructure.redis.core;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public class GsonRedisSerializer<T> implements RedisSerializer<T> {
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	@SuppressWarnings("unused")
	private ObjectMapper objectMapper = new ObjectMapper();
	private Class<T> clazz;

	public GsonRedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return JosnRedisUtil.toJson(t).getBytes(DEFAULT_CHARSET);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		String str = new String(bytes, DEFAULT_CHARSET);
	
		return JosnRedisUtil.toBean(str, clazz);
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}
}
