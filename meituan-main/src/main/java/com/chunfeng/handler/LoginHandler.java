package com.chunfeng.handler;

import com.alibaba.fastjson2.JSON;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.contants.UserConstants;
import com.chunfeng.entity.Router;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.utils.ExcludeUrlUtils;
import com.chunfeng.utils.RedisClientsUtils;
import com.chunfeng.utils.TokenUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 登录拦截器
 *
 * @author by 春风能解释
 * <p>
 * 2026/9/30
 */
@Component
@Slf4j
@WebFilter
@RequiredArgsConstructor
public class LoginHandler implements Filter {
    /**
     * Redis工具
     */
    private final RedisClientsUtils redisClientsUtils;

    /**
     * 登录拦截
     *
     * @param request  请求体
     * @param response 响应体
     * @param chain    拦截器链
     */
    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        // 获取请求路径
        String url = servletRequest.getRequestURI();
        // 判断是否排除
        if (ExcludeUrlUtils.isExcludeUrl(url) != 0) {
            log.warn("拦截器已放行!路径:{}", url);
            // 直接放行
            chain.doFilter(request, response);
            return;
        }
        // 获取请求头
        String token = servletRequest.getHeader(UserConstants.AUTHORIZATION);
        // 判断token是否为空
        if (!StringUtils.hasText(token)) {
            log.warn("拦截器已拦截此路径:{},原因:Token为空", url);
            // 抛出异常
            throw new ServiceException(RequestException.FORBIDDEN);
        }
        // 获取用户id
        Long userId = TokenUtils.checkToken(token, Long.class);
        // 判断token是否一致
        String cacheToken = redisClientsUtils.get(CacheKeysConstants.UserKeys.USER_TOKEN + userId);
        // token不一致
        if (!token.equals(cacheToken)) {
            log.warn("拦截器已拦截此路径:{},原因:Token不可用", url);
            throw new ServiceException(RequestException.FORBIDDEN);
        }
        // 续期
        redisClientsUtils.reTtl(CacheKeysConstants.UserKeys.USER_TOKEN + userId, CacheKeysConstants.CacheTimes.TOKEN_TIME);
        // 获取请求方法
        String method = servletRequest.getMethod();
        // 获取redis中保存的路由列表
        String value = redisClientsUtils.get(CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY + token);
        List<Router> routers = List.of();
        // 判断路由列表是否为空
        if (!value.isEmpty()) {
            // 反序列化路由列表
            routers = JSON
                    .parseArray(value, Router.class);
            // 续期
            redisClientsUtils.reTtl(CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY + token, CacheKeysConstants.CacheTimes.PERMISSION_LIST);
        }
        // 判断请求是否在路由列表中
        if (!ExcludeUrlUtils.isMatch(new Router()
                .setMethod(method)
                .setUri(url), routers)) {
            log.warn("拦截器已拦截:{}-{},权限不足!", url, method);
            // 抛出异常
            throw new ServiceException(RequestException.UNAUTHORIZED);
        }
        // 更新当前用户ID
        redisClientsUtils.setGlobalData(UserConstants.ID, userId);
        log.info("拦截器已通过!路径：{},方法:{}", url, method);
        // 放行
        chain.doFilter(request, response);
    }
}
