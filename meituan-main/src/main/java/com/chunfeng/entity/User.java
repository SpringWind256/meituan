package com.chunfeng.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户表
 *
 * @author by 春风能解释
 * <p>
 * 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "用户表", description = "用户表")
public class User extends BaseEntity implements Serializable {
    /**
     * 序列化字段
     */
    @Serial
    private static final long serialVersionUID = 367356027511564599L;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
    /**
     * 年龄
     */
    @Schema(description = "年龄")
    private Integer age;
    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;
    /**
     * 性别(0女,1男,2保密)
     */
    @Schema(description = "性别(0女,1男,2保密)")
    private Integer sex;
    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;
    /**
     * QQ号
     */
    @Schema(description = "QQ号")
    private String qq;
    /**
     * 微信号
     */
    @Schema(description = "微信号")
    private String wechat;
    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;
    /**
     * 头像
     */
    @Schema(description = "头像")
    private Long avatar;
    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;
    /**
     * 状态(0正常,1冻结)
     */
    @Schema(description = "状态(0正常,1冻结)")
    private Integer status;
}