package com.chunfeng.contants;

/**
 * 公共常量
 *
 * @author by 春风能解释
 * <p>
 * 2024/10/22
 */
public interface CommonConstants {

    /**
     * 总页数的key名
     */
    String PAGE_KEY_NAME = "pageSize";

    /**
     * 默认的序列化类扫描
     */
    String[] SERIALIZE_PACKAGES = {
            "com.chunfeng.entity.***",
            "com.chunfeng.vo.***",
            "com.chunfeng.dto.***",
            "com.chunfeng.base.***",
            "com.chunfeng.result.***",
            "com.chunfeng.statistics.***"
    };
}
