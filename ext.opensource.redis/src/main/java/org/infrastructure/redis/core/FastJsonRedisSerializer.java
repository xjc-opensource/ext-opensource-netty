package org.infrastructure.redis.core;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.util.Assert;
import java.nio.charset.Charset;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
    @SuppressWarnings("unused")
	private ObjectMapper objectMapper = new ObjectMapper();
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;
    
   static {
	   ParserConfig.getGlobalInstance().setAsmEnable(true);
   		//ParserConfig.getGlobalInstance().setAutoTypeSupport(true); 
   		//如果遇到反序列化autoType is not support错误，请添加并修改一下包名到bean文件路径
       // ParserConfig.getGlobalInstance().addAccept("com.xxxxx.xxx");
    }
    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
        //return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz);
    }
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}