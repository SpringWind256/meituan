package com.chunfeng.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 容器配置
 *
 * @author by 春风能解释
 * <p>
 * 2026/9/30
 */
@Configuration
@Slf4j
@ServletComponentScan
public class WebConfiguration {


    // /**
    //  * 注册 过滤器 Filter
    //  *
    //  * @param loginHandler 拦截器
    //  */
    // @Bean
    // public FilterRegistrationBean<Filter> webVisitFilterConfigRegistration(LoginHandler loginHandler) {
    //     //匹配拦截 URL
    //     FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
    //     registration.setDispatcherTypes(DispatcherType.REQUEST);
    //     registration.setFilter(loginHandler);
    //     registration.addUrlPatterns("/**");
    //     //设置过滤器链执行顺序
    //     registration.setOrder(0);
    //     //启动标识
    //     registration.setEnabled(true);
    //     log.info("拦截器已注册成功!");
    //     return registration;
    // }
}
