package com.chunfeng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改手机号
 *
 * @author by 春风能解释
 * <p>
 * 2024/9/21
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "修改手机号")
public class UserUpdatePhoneDto implements Serializable {

    /**
     * 序列化ID
     */
    @Serial
    private static final long serialVersionUID = 2663250411504363296L;
    /**
     * 手机号
     */
    @Schema(description = "原手机号")
    private String oldPhone;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;

    /**
     * 新手机号
     */
    @Schema(description = "新手机号")
    private String newPhone;

    /**
     * 新手机号验证码
     */
    @Schema(description = "新手机号验证码")
    private String newCode;

}
