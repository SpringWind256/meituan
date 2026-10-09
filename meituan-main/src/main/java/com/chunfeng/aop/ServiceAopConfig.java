package com.chunfeng.aop;

import com.chunfeng.contants.AopConstants;
import com.chunfeng.note.ExcludeMethods;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局事务+日志处理
 * <p>
 * 用于配置Spring事务与日志收集机制，该配置采用了AOP切面编程思想
 * <p>
 * 该类默认实现了全局事务与日志收集,其具体实现原理如下:
 * <ul>
 * <li>
 * 事务处理,类型采用{@link TransactionDefinition}中的PROPAGATION_REQUIRES_NEW类型
 * <p>
 * 触发时机为业务层的任意方法发生异常或返回类型的success属性为{@code false}时执行回滚
 * <p>
 * *默认生效方法范围不包含:look、get、query、list、page开头的方法
 * <p>
 * 注解{@link ExcludeMethods}用于指定业务层中默认的不需要执行事务的方法
 * <p>
 *  只要方法中加入此注解,该类将不再参与事务处理
 * <p>
 * 具体可参照{@link JsonRequest}类
 * </li>
 * <li>
 * 日志收集,现暂时只进行控制台输出,方便开发者定位异常位置
 * <p>
 * 生效的方法范围包含全体业务层方法
 * </ul>
 *
 * @author by 春风能解释
 * <p>
 * 2023/8/1
 */
@Aspect
@Configuration
@Slf4j
@RequiredArgsConstructor
public class ServiceAopConfig {

    /**
     * 事务管理器
     */
    private final PlatformTransactionManager platformTransactionManager;

    /**
     * 事务管理器
     */
    private final TransactionDefinition transactionDefinition;

    /**
     * 初始化通知
     *
     * @param pjp 切入点对象
     */
    private Map<String, String> initAop(JoinPoint pjp) {
        Map<String, String> map = new ConcurrentHashMap<>();
        // 获取目标对象
        Object pjpTarget = pjp.getTarget();
        // 反射
        Class<?> aClass = pjpTarget.getClass();
        // 拿到切入点的对象名称
        String objectName = aClass.getName();
        // 拿到方法名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodName = signature.getName();
        // 对象信息
        map.put(AopConstants.CLASS_NAME, objectName);
        // 方法名
        map.put(AopConstants.METHOD_NAME, methodName);
        // 参数
        Object[] args = pjp.getArgs();
        if (args.length == 0) {
            return map;
        }
        // 参数
        map.put(AopConstants.METHOD_PARAMS, Arrays.toString(args));
        return map;
    }

    /**
     * 事务处理
     *
     * @param joinPoint 切入点
     * @return 执行结果
     */
    @SneakyThrows
    @Around(AopConstants.TRANSACTION_POINTCUT)
    public Object transaction(ProceedingJoinPoint joinPoint) {
        // 开启事务
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        // 执行逻辑
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            // 回滚事务
            platformTransactionManager.rollback(transaction);
            throw new ServiceException(throwable);
        }
        // 提交事务
        platformTransactionManager.commit(transaction);
        return proceed;
    }

    /**
     * 日志输出
     *
     * @param joinPoint 方法
     * @return 返回值
     */
    @Around(AopConstants.LOG_POINTCUT)
    private Object log(ProceedingJoinPoint joinPoint) {
        Map<String, String> map = initAop(joinPoint);
        Object proceed;
        long start, end;
        start = System.currentTimeMillis();
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("\n方法名:{}@{}\n状态:异常!\n原因:{}\n当前线程:{}\n方法入参列表:{}",
                    map.get(AopConstants.CLASS_NAME), map.get(AopConstants.METHOD_NAME),
                    e.getLocalizedMessage(),
                    Thread.currentThread().getName(),
                    map.get(AopConstants.METHOD_PARAMS));
            throw new ServiceException(e);
        }
        end = System.currentTimeMillis();
        log.info("\n方法名:{}@{}\n状态:完成!\n共耗时:{}ms\n当前线程:{}\n方法入参列表:{}",
                map.get(AopConstants.CLASS_NAME), map.get(AopConstants.METHOD_NAME),
                end - start,
                Thread.currentThread().getName(),
                map.get(AopConstants.METHOD_PARAMS));
        return proceed;
    }
}
