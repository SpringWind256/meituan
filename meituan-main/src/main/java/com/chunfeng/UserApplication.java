package com.chunfeng;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * 用户模块启动类
 *
 * @author by 春风能解释
 * <p>
 * 2024/6/4
 */
@SpringBootApplication
@Slf4j
public class UserApplication {
    /**
     * 启动方法
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(UserApplication.class, args);
        log.info("模块启动成功！--->{}", app.getDisplayName());
    }
}
