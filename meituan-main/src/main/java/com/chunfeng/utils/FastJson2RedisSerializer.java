package com.chunfeng.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.chunfeng.contants.CommonConstants;
import com.chunfeng.result.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Objects;

/**
 * fastjson Redis序列化配置
 *
 * @param <T> 类型
 * @author 北京城市开发者社区
 */
// https://blog.csdn.net/qq_43520746/article/details/133695597
@Slf4j
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

    /**
     * 自动扫描
     */
    public static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(CommonConstants.SERIALIZE_PACKAGES);

    /**
     * 当前传入的类型
     */
    private final Class<T> clazz;

    /**
     * 传入类型的构造
     *
     * @param clazz 类型
     */
    public FastJson2RedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 对象转字节数组
     *
     * @param t 对象
     * @return 字节数组
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (Objects.isNull(t)) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(t, JSONWriter.Feature.WriteClassName);
        } catch (Exception e) {
            log.error("Fastjson2 序列化错误：{}", e.getMessage());
            throw new ServiceException(e);
        }

    }

    /**
     * 字节数组转对象
     *
     * @param bytes 字节数组
     * @return 对象
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return JSON.parseObject(bytes, clazz, AUTO_TYPE_FILTER);
        } catch (Exception e) {
            log.error("Fastjson2 反序列化错误：{}", e.getMessage());
            throw new ServiceException(e);
        }

    }
}
