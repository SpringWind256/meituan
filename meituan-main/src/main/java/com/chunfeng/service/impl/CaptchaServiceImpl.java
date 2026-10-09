package com.chunfeng.service.impl;

import cn.hutool.core.util.IdUtil;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.contants.UserConstants;
import com.chunfeng.note.ExcludeMethods;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.service.ICaptchaService;
import com.chunfeng.utils.RedisClientsUtils;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码业务实现
 *
 * @author by 春风能解释
 * <p>
 * 2024/6/21
 */
@Service
public class CaptchaServiceImpl implements ICaptchaService {
    /**
     * 默认验证码配置注入
     */
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    /**
     * 算数验证码配置注入
     */
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    /**
     * 注入Redis工具类
     */
    @Resource
    private RedisClientsUtils redisClientsUtils;

    /**
     * 获取验证码
     *
     * @param type 验证码类型(1默认,2算数类型)
     * @return JSON
     */
    @Override
    public JsonRequest<String> getCaptcha(Integer type) {
        // 随机生成UUID
        String uuid = IdUtil.fastSimpleUUID();
        String text;
        String result = "";
        BufferedImage image = null;
        switch (type) {
            case 1 -> {
                // 创建验证码
                result = captchaProducer.createText();
                text = result;
                // 生成验证码图片
                image = captchaProducer.createImage(text);
            }
            case 2 -> {
                text = captchaProducerMath.createText();
                // 结果
                result = text.substring(text.lastIndexOf("@") + 1);
                // 输出的文字
                text = text.substring(0, text.lastIndexOf("@"));
                // 生成图片
                image = captchaProducerMath.createImage(text);
            }
        }
        if (image == null) {
            throw new ServiceException(RequestException.CAPTCHA_GENERATED_ERROR);
        }
        Map<String, Object> map;
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            ImageIO.write(image, UserConstants.CAPTCHA_TYPE, os);
            // 存入Redis
            redisClientsUtils.set(CacheKeysConstants.UserKeys.CODE_ARRAY + uuid, result, CacheKeysConstants.CacheTimes.CODE_TIME);
            // 创建图片流map
            map = new HashMap<>();
            String imageBase64 = new String(Base64.getEncoder()
                    .encode(os.toByteArray()));
            // 输出
            map.put(UserConstants.CAPTCHA_KEY, UserConstants.CAPTCHA_BODY_PREFIX + imageBase64);
        } catch (IOException e) {
            throw new ServiceException(RequestException.CAPTCHA_GENERATED_ERROR);
        }
        return JsonRequest.success(uuid, map);
    }

    /**
     * 校验验证码
     *
     * @param uuid redis缓存键
     * @param code 待检验的验证码
     * @return JSON
     */
    @Override
    @ExcludeMethods
    public JsonRequest<Boolean> verifyCaptcha(String uuid, String code) {
        if (!StringUtils.hasText(uuid) || !StringUtils.hasText(code)) {
            throw new ServiceException(RequestException.CAPTCHA_VERIFY_ERROR);
        }
        // 获取验证码
        String captcha = redisClientsUtils.get(CacheKeysConstants.UserKeys.CODE_ARRAY + uuid);
        if (!StringUtils.hasText(captcha) || !captcha.equals(code)) {
            throw new ServiceException(RequestException.CAPTCHA_VERIFY_ERROR);
        }
        // 成功后删除原验证码
        redisClientsUtils.remove(uuid);
        return JsonRequest.success(true);
    }
}
