package com.chunfeng.utils;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * 基于ThreadLocal封装的工具类，用户保存和获取当前登录用户id
 *
 * @author 小雷
 * <p>
 * 2023-11-14 00:21:29
 */
public class BaseContext {
    /**
     * 当前线程
     */
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();
    /**
     * 不直接输出字符串
     */
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取id值
     *
     * @return 返回当前ID
     * @deprecated 分布式下不同服务无法获取到同一个ID
     */
    @Deprecated
    public static Long getCurrentId() {
        return THREAD_LOCAL.get() == null ? 0 : THREAD_LOCAL.get();
    }

    /**
     * 设置id值
     *
     * @param id 设置当前ID
     * @deprecated 分布式下不同服务无法获取到同一个ID
     */
    @Deprecated
    public static void setCurrentId(Long id) {
        THREAD_LOCAL.set(id);
    }

    /**
     * 获取当前时间
     *
     * @return 格式化后的当前时间字符串
     */
    public static String date() {
        return LocalDateTimeUtil.formatNormal(LocalDateTime.now());
    }
}
