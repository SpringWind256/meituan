package com.chunfeng.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis的配置键
 *
 * @author by 春风能解释
 * <p>
 * 2023/11/17
 */
@Component
@ConfigurationProperties(prefix = "redis.config")
@Data
@NoArgsConstructor
public class RedisProperties {
    /**
     * redis缓存存在时间(分钟为单位)
     */
    private Long time = 300L;
}
