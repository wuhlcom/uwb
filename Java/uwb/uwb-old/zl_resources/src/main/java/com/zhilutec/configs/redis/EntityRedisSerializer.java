package com.zhilutec.configs.redis;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

//redis超时注解
public class EntityRedisSerializer implements RedisSerializer<Object>{

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return SerializeUtils.serialize(t);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return SerializeUtils.unserialize(bytes);
    }

}