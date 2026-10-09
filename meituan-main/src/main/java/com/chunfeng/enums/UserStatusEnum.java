package com.chunfeng.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户状态枚举
 *
 * @author by 春风能解释
 * <p>
 * 2024/10/18
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserStatusEnum {

    /**
     * 正常
     */
    NORMAL(0, "正常"),
    /**
     * 冻结中
     */
    FREEZE(1, "冻结中");
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
