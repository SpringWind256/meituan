package com.chunfeng.utils;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson2.JSON;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.properties.RedisProperties;
import com.chunfeng.result.exception.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Redis执行工具类
 *
 * @author by 春风能解释
 * <p>
 * 2022/10/11
 */
@Component
@Slf4j
public class RedisClientsUtils {
    /**
     * Redis客户端
     */
    @Resource(name = "customRedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;
    /**
     * redis配置
     */
    @Resource
    private RedisProperties redisProperties;

    /**
     * 存值
     *
     * @param key       键
     * @param value     值
     * @param isForever 是否永久
     */
    public void set(String key, Object value, boolean isForever) {
        if (isForever) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key,
                    value,
                    RedisTimeUtils.randomTtl(redisProperties.getTime()));
        }
        log.info("已将{}-{}存入redis", key, value);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(value)) {
            log.warn("未获取到key为{}的值!", key);
            value = "";
            return value.toString();
        }
        log.info("已获取到{}-{}的值", key, value);
        return value.toString();
    }

    /**
     * 获取值
     *
     * @param resultClass 结果泛型
     * @param key         键
     * @param <T>         结果泛型
     * @return 值
     */
    public <T> T get(String key, Class<T> resultClass) {
        return JSON.parseObject(get(key), resultClass, FastJson2RedisSerializer.AUTO_TYPE_FILTER);
    }

    /**
     * 获取值
     *
     * @param key         键
     * @param resultClass 结果泛型
     * @param <T>         结果泛型
     * @return 值
     */
    public <T> List<T> getList(String key, Class<T> resultClass) {
        return JSON.parseArray(get(key), resultClass);
    }

    /**
     * 删除键
     *
     * @param key 键
     * @return 是否成功
     */
    public Boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 存入Map集合
     *
     * @param root  根键
     * @param key   key
     * @param value 值
     */
    public void setMap(String root, String key, Object value) {
        redisTemplate.opsForHash()
                .put(root, key, value);
    }

    /**
     * 获取值
     *
     * @param root 根键
     * @param key  key
     * @return 值
     */
    public String getMap(String root, String key) {
        return (String) redisTemplate.opsForHash()
                .get(root, key);
    }

    /**
     * 删除指定键
     *
     * @param root 根键
     * @param key  key
     */
    public void deleteMap(String root, String key) {
        redisTemplate.opsForHash()
                .delete(root, key);
    }

    /**
     * 指定时间存值
     *
     * @param key   键
     * @param value 值
     * @param time  时间(分钟)
     */
    public void set(String key, Object value, Long time) {
        redisTemplate.opsForValue().set(key,
                value,
                time,
                TimeUnit.MINUTES);
        log.info("已将{}-{}存入redis", key, value);
    }

    /**
     * 指定时间存值
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     */
    public void set(String key, Object value, Duration time) {
        redisTemplate.opsForValue().set(key,
                value,
                time);
        log.info("已将{}-{}存入redis", key, value);
    }

    /**
     * 缓存续期
     *
     * @param key 键
     */
    public void reTtl(String key) {
        reTtl(key, RedisTimeUtils.randomTtl(redisProperties.getTime()));
    }


    /**
     * 缓存续期
     *
     * @param key  键
     * @param time 时间
     */
    public void reTtl(String key, Duration time) {
        // 获取值
        Object value = redisTemplate.opsForValue()
                .get(key);
        if (Objects.isNull(value)) {
            log.warn("未获取到key为{}的值!", key);
            return;
        }
        // 重新设置过期时间
        redisTemplate.opsForValue()
                .set(key, value, time);
        log.info("已续期{}-{}", key, value);
    }

    /**
     * 存值
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间
     * @param unit  时间单位
     * @param <T>   泛型
     */
    public <T> void set(String key, T value, Long time, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, time, unit);
    }

    /**
     * 取值(解决缓存穿透问题)
     * <p>
     * 缓存key = keyPrefix + params
     *
     * @param keyPrefix     键前缀
     * @param params        参数
     * @param resultClass   结果泛型
     * @param selectMethods 查询方法
     * @param <P>           参数泛型
     * @param <T>           结果泛型
     * @return 值
     */
    public <P, T> T get(String keyPrefix, P params, Function<P, T> selectMethods, Class<T> resultClass) {
        return get(keyPrefix, params, selectMethods, resultClass, RedisTimeUtils.randomTtlValue(redisProperties.getTime()), TimeUnit.MINUTES);
    }

    /**
     * 批量取值(解决缓存穿透问题)
     * <p>
     * 缓存key = keyPrefix + params
     *
     * @param keyPrefix     键前缀
     * @param params        参数
     * @param resultClass   结果泛型
     * @param selectMethods 查询方法
     * @param <P>           参数泛型
     * @param <T>           结果泛型
     * @return 值
     */
    public <P, T> List<T> gets(String keyPrefix, P params, Function<P, List<T>> selectMethods, Class<T> resultClass) {
        return gets(keyPrefix, params, selectMethods, resultClass, RedisTimeUtils.randomTtlValue(redisProperties.getTime()), TimeUnit.MINUTES);
    }

    /**
     * 批量取值(解决缓存穿透问题)
     * <p>
     * 缓存key = keyPrefix + params
     *
     * @param keyPrefix     键前缀
     * @param params        参数
     * @param selectMethods 查询方法
     * @param resultClass   结果泛型
     * @param time          过期时间
     * @param unit          时间单位
     * @param <P>           参数泛型
     * @param <T>           结果泛型
     * @return 值
     */
    public <P, T> List<T> gets(String keyPrefix, P params, Function<P, List<T>> selectMethods, Class<T> resultClass, Long time, TimeUnit unit) {
        String key = keyPrefix + params;
        // 先查询缓存
        Object value = redisTemplate.opsForValue()
                .get(key);
        // 缓存命中空值
        if (isBank(value)) {
            return null;
        }
        // 缓存命中值
        if (Objects.nonNull(value)) {
            return JSON.parseArray(JSON.toJSONString(value), resultClass);
        }
        // 查询数据库
        List<T> result = selectMethods.apply(params);
        // 结果为空则存入空值
        if (Objects.isNull(result)) {
            // 存入空值
            set(key, "", time, unit);
            return null;
        }
        // 存入缓存
        set(key, result, time, unit);
        return result;
    }

    /**
     * 取值(解决缓存穿透问题)
     * <p>
     * 缓存key = keyPrefix + params
     *
     * @param keyPrefix     键前缀
     * @param params        参数
     * @param selectMethods 查询方法
     * @param resultClass   结果泛型
     * @param time          过期时间
     * @param unit          时间单位
     * @param <P>           参数泛型
     * @param <T>           结果泛型
     * @return 值
     */
    public <P, T> T get(String keyPrefix, P params, Function<P, T> selectMethods, Class<T> resultClass, Long time, TimeUnit unit) {
        String key = keyPrefix + params;
        // 先查询缓存
        Object value = redisTemplate.opsForValue()
                .get(key);
        // 缓存命中空值
        if (isBank(value)) {
            return null;
        }
        // 缓存命中值
        if (Objects.nonNull(value)) {
            return JSON.parseObject(JSON.toJSONString(value), resultClass);
        }
        // 查询数据库
        T result = selectMethods.apply(params);
        // 结果为空则存入空值
        if (Objects.isNull(result)) {
            // 存入空值
            set(key, "", time, unit);
            return null;
        }
        // 存入缓存
        set(key, result, time, unit);
        return result;
    }

    /**
     * 判断字符串是否为空字符
     *
     * @param value 字符串
     * @return 是否为空字符
     */
    private boolean isBank(Object value) {
        return Objects.nonNull(value) && value.toString().isEmpty();
    }

    /**
     * 取值(解决缓存击穿问题)
     *
     * @param keyPrefix     键前缀
     * @param resultClass   结果泛型
     * @param params        参数
     * @param selectMethods 查询方法
     * @param <P>           参数泛型
     * @param <T>           结果泛型
     * @return 值
     */
    public <P, T> T get(String keyPrefix, P params, Class<T> resultClass, Function<P, T> selectMethods) {
        return get(keyPrefix, params, resultClass, selectMethods, RedisTimeUtils.randomTtlValue(redisProperties.getTime()), TimeUnit.MINUTES);
    }

    /**
     * 取值(解决缓存击穿问题)
     *
     * @param keyPrefix     键前缀
     * @param params        参数
     * @param selectMethods 查询方法
     * @param time          过期时间
     * @param unit          时间单位
     * @param <P>           参数泛型
     * @param resultClass   结果泛型
     * @param <T>           结果泛型
     * @return 值
     */
    public <P, T> T get(String keyPrefix, P params, Class<T> resultClass, Function<P, T> selectMethods, Long time, TimeUnit unit) {
        T result;
        try {
            String key = keyPrefix + params;
            // 先查询缓存
            Object value = redisTemplate.opsForValue()
                    .get(key);
            // 缓存命中空值
            if (isBank(value)) {
                return null;
            }
            // 缓存命中值
            if (Objects.nonNull(value)) {
                return JSON.parseObject(JSON.toJSONString(value), resultClass);
            }
            // 获取互斥锁
            if (tryLock(params.toString())) {
                // 失败则休眠
                Thread.sleep(50);
                return get(keyPrefix, params, resultClass, selectMethods, time, unit);
            }
            // 先查询缓存
            Object value1 = redisTemplate.opsForValue()
                    .get(key);
            // 缓存命中
            if (Objects.nonNull(value1)) {
                return JSON.parseObject(JSON.toJSONString(value1), resultClass);
            }
            // 不存在查询数据库
            result = selectMethods.apply(params);
            if (Objects.isNull(result)) {
                // 写入空值
                set(key, "", time, unit);
                // MySQL不存在返回错误信息
                return null;
            }
            set(key, result, time, unit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 释放互斥锁
            unlock(params.toString());
        }
        // 返回缓存数据
        return result;
    }

    /**
     * 尝试加锁
     *
     * @param key 锁的key
     * @return true:加锁成功 false:加锁失败
     */
    private boolean tryLock(String key) {
        return BooleanUtil.isFalse(redisTemplate
                .opsForValue()
                .setIfAbsent(CacheKeysConstants.CommonKeys.GLOBAL_LOCK + key, "1", 10, TimeUnit.SECONDS));
    }

    /**
     * 释放锁
     *
     * @param key 锁的key
     */
    private void unlock(String key) {
        redisTemplate.delete(CacheKeysConstants.CommonKeys.GLOBAL_LOCK + key);
    }

    /**
     * 设置全局数据
     *
     * @param key   设置的key
     * @param value 设置的值
     */
    public void setGlobalData(String key, Object value) {
        // 获取互斥锁
        try {
            if (tryLock(key)) {
                // 失败则休眠
                Thread.sleep(50);
                setGlobalData(key, value);
            }
            setMap(CacheKeysConstants.CommonKeys.GLOBAL_DATA, key, value);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            // 释放互斥锁
            unlock(key);
        }

    }

    /**
     * 获取全局数据
     *
     * @return 结果
     */
    public Map<Object, Object> getGlobalData() {
        return redisTemplate
                .opsForHash()
                .entries(CacheKeysConstants.CommonKeys.GLOBAL_DATA);
    }

    /**
     * 获取全局数据
     *
     * @param <T>  结果
     * @param key  键
     * @param type 值类型
     * @return 结果
     */
    public <T> T getGlobalData(String key, Class<T> type) {
        try {
            if (tryLock(key)) {
                // 失败则休眠
                Thread.sleep(50);
                // 递归
                getGlobalData(key, type);
            }
            Map<Object, Object> globalData = getGlobalData();
            Object value = globalData.get(key);
            if (Objects.isNull(value)) {
                return null;
            }
            return JSON.parseObject(JSON.toJSONString(value), type);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            unlock(key);
        }
    }

}
