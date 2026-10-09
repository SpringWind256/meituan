package com.chunfeng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户修改密码的参数
 *
 * @author by 春风能解释
 * <p>
 * 2024/9/20
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "用户修改密码Dto")
public class UserUpdatePasswordDto implements Serializable {

    /**
     * 序列化ID
     */
    @Serial
    private static final long serialVersionUID = -21365740673680568L;

    /**
     * 手机号
     */
    @Schema(name = "手机号")
    private String phone;
    /**
     * 旧密码
     */
    @Schema(name = "旧密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @Schema(name = "新密码")
    private String newPassword;

    /**
     * 验证码
     */
    @Schema(name = "验证码")
    private String code;
}
