package com.chunfeng.service;

import com.chunfeng.result.JsonRequest;

/**
 * 验证码业务接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/6/21
 */
public interface ICaptchaService {

    /**
     * 获取验证码
     *
     * @param type 验证码类型(1默认,2算数类型)
     * @return JSON
     */
    JsonRequest<String> getCaptcha(Integer type);

    /**
     * 校验验证码
     *
     * @param uuid redis缓存键
     * @param code 待检验的验证码
     * @return JSON
     */
    JsonRequest<Boolean> verifyCaptcha(String uuid, String code);
}
