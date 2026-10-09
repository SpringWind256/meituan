package com.chunfeng.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 性别枚举
 *
 * @author by 春风能解释
 * <p>
 * 2024/9/20
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserSexEnum {
    /**
     * 女
     */
    WOMAN(0, "女"),
    /**
     * 男
     */
    MAN(1, "男"),
    /**
     * 保密
     */
    UNKNOWN(2, "保密");
    /**
     * 值
     */
    @EnumValue
    private Integer value;
    /**
     * 显示值
     */
    @JsonValue
    private String name;

}
