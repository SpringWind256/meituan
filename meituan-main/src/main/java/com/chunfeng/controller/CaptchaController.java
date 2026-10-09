package com.chunfeng.controller;

import com.chunfeng.result.JsonRequest;
import com.chunfeng.service.ICaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码控制器
 *
 * @author by 春风能解释
 * <p>
 * 2024/6/21
 */
@RestController
@RequestMapping("/captcha")
@Tag(name = "验证码控制类", description = "验证码操作访问接口")
public class CaptchaController {

    /**
     * 验证码服务
     */
    @Resource
    private ICaptchaService captchaService;

    /**
     * 获取验证码
     *
     * @param type 验证码类型(1默认,2算数类型)
     * @return JSON
     */
    @GetMapping("/captcha")
    @Operation(summary = "获取验证码")
    public JsonRequest<String> getCaptcha(
            @Parameter(description = "验证码类型(1默认,2算数类型)", required = true)
            @RequestParam Integer type) {
        return captchaService.getCaptcha(type);
    }

    /**
     * 校验验证码
     *
     * @param uuid redis缓存键
     * @param code 待检验的验证码
     * @return JSON
     */
    @PostMapping("/verify")
    @Operation(summary = "校验验证码")
    public JsonRequest<Boolean> verifyCaptcha(
            @Parameter(description = "redis缓存键", required = true)
            @RequestParam String uuid,
            @Parameter(description = "待检验的验证码", required = true)
            @RequestParam String code) {
        return captchaService.verifyCaptcha(uuid, code);
    }
}
