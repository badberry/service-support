package cn.cloudtop.strawberry.service.caches;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Created by jackie on 16-6-17
 */
public class ClassTypeJsonRedisSerializer implements RedisSerializer<Object> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String CLASS_OBJECT_BREAKER = "@@";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return new byte[0];
        }
        try {
            String resultStr = new StringBuilder()
                    .append(o.getClass().getName())
                    .append(CLASS_OBJECT_BREAKER)
                    .append(objectMapper.writeValueAsString(o))
                    .toString();
            return resultStr.getBytes(DEFAULT_CHARSET);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            String typedStr = new String(bytes, DEFAULT_CHARSET);
            String[] classObjectPairs = typedStr.split(CLASS_OBJECT_BREAKER, 2);
            if (classObjectPairs.length != 2) {
                throw new ClassNotFoundException("typed String is not contains " + CLASS_OBJECT_BREAKER);
            }
            Class<?> clazz = Class.forName(classObjectPairs[0]);
            return objectMapper.readValue(classObjectPairs[1], clazz);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}
