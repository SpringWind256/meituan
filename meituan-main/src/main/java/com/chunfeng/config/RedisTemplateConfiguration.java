package com.chunfeng.config;

import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import com.chunfeng.utils.FastJson2RedisSerializer;
import com.chunfeng.utils.RedisTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

/**
 * Redis核心序列化配置
 *
 * @author 北京城市开发者社区
 */
@Configuration
@Slf4j
@EnableCaching
// https://blog.csdn.net/qq_43520746/article/details/133695597
public class RedisTemplateConfiguration {
    /**
     * Redis工具
     *
     * @param redisConnectionFactory redis连接工厂
     * @return redis工具类
     */
    @Bean(name = "customRedisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);
        StringRedisSerializer serializer = new StringRedisSerializer();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setValueSerializer(fastJson2RedisSerializer);
        template.setHashValueSerializer(fastJson2RedisSerializer);
        template.afterPropertiesSet();
        log.info("Redis工具序列化配置就绪!");
        return template;
    }

    /**
     * SpringCache序列化
     *
     * @param cacheProperties 序列化配置
     * @return 配置
     */
    @Bean
    // https://springdoc.cn/spring-cache-redis-json/
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        // 先载入配置文件中的配置信息
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        // 根据配置文件中的定义，初始化 Redis Cache 配
        if (redisProperties.getTimeToLive() != null) {
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(
                    RedisTimeUtils.randomTtl(redisProperties// 设置随机时间防止缓存雪崩
                            .getTimeToLive()
                            .toMinutes()));
        }
        if (StringUtils.hasText(redisProperties.getKeyPrefix())) {
            redisCacheConfiguration = redisCacheConfiguration.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            redisCacheConfiguration = redisCacheConfiguration.disableKeyPrefix();
        }
        // Fastjson 的通用 RedisSerializer 实现
        GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();
        log.info("SpringCache序列化配置就绪!");
        // 设置 Value 的序列化方式
        return redisCacheConfiguration
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }


}
