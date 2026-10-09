package com.chunfeng.contants;

/**
 * 正则常量
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/21
 */
public interface RegexConstants {
    /**
     * 邮箱正则
     */
    String EMAIL_REGEX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 手机正则
     */
    String PHONE_REGEX = "^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$";
    /**
     * 身份证正则
     */
    String ID_CARD_REGEX = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
    /**
     * QQ正则
     */
    String QQ_REGEX = "[1-9][0-9]{4,}";
    /**
     * IP地址正则
     */
    String IP_REGEX = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
}
