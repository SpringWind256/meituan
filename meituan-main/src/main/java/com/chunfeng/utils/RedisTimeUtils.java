package com.chunfeng.utils;

import com.chunfeng.contants.CacheKeysConstants;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * redis的额外工具类
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/4
 */
public class RedisTimeUtils {

    /**
     * 随机Ttl(解决缓存雪崩问题)
     *
     * @param baseTtl 基础ttl
     * @return 时间
     */
    // https://blog.csdn.net/qq_65052774/article/details/134006163
    public static Duration randomTtl(Long baseTtl) {
        // 范围
        return Duration.ofMinutes(randomTtlValue(baseTtl));
    }

    /**
     * 随机Ttl(解决缓存雪崩问题)
     *
     * @param baseTtl 基础ttl
     * @return 时间
     */
    // https://blog.csdn.net/qq_65052774/article/details/134006163
    public static Long randomTtlValue(Long baseTtl) {
        // 范围
        return baseTtl +
                ThreadLocalRandom.current()
                        .nextInt(-CacheKeysConstants.CacheTimes.RANDOM_RANGE, CacheKeysConstants.CacheTimes.RANDOM_RANGE);
    }
}
