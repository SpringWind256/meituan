package com.chunfeng.contants;

import cn.hutool.core.codec.Base64;

/**
 * 常量配置
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/20
 */
public interface UserConstants {
    /**
     * 令牌头
     */
    String AUTHORIZATION = "Authorization";
    /**
     * ID
     */
    String ID = "id";
    /**
     * 验证码类型
     */
    String CAPTCHA_TYPE = "png";
    /**
     * 验证码KEY
     */
    String CAPTCHA_KEY = "image";
    /**
     * 验证码主体前缀
     */
    String CAPTCHA_BODY_PREFIX = String.format("data:image/%s;base64,", CAPTCHA_TYPE);

    /**
     * 文件请求处理器的属性
     */
    String ATTR_FILE = "NON-STATIC-FILE";

    /**
     * token过期时间(天)
     */
    Integer TOKEN_EXPIRE_DAY = 3;

    /**
     * token密钥
     */
    // springwind@2516649281$ --> base64
    String TOKEN_SECRET = Base64.encode("springwind@2516649281$");

}
