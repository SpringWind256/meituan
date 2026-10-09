package com.chunfeng.contants;

/**
 * AOP切面常量
 *
 * @author by 春风能解释
 * <p>
 * 2024/10/2
 */
public interface AopConstants {
    /**
     * 业务层包名
     */
    String BASE_PACKAGE = "com.chunfeng.service.impl";

    /**
     * 日志切入点
     */
    String LOG_POINTCUT = "execution(* " + BASE_PACKAGE + ".*.*(..))";

    /**
     * 事务切入点
     */
    String TRANSACTION_POINTCUT = LOG_POINTCUT +
            "&&!@annotation(com.chunfeng.note.ExcludeMethods)" +
            "&&!execution(* " + BASE_PACKAGE + ".*.query*(..))" +
            "&&!execution(* " + BASE_PACKAGE + ".*.list*(..))" +
            "&&!execution(* " + BASE_PACKAGE + ".*.page*(..))" +
            "&&!execution(* " + BASE_PACKAGE + ".*.look*(..))" +
            "&&!execution(* " + BASE_PACKAGE + ".*.get*(..))" +
            "&&!execution(* " + BASE_PACKAGE + ".*.save*(..))";
    /**
     * 方法名
     */
    String METHOD_NAME = "methodName";

    /**
     * 方法参数
     */
    String METHOD_PARAMS = "methodParams";

    /**
     * 类名
     */
    String CLASS_NAME = "className";
}
