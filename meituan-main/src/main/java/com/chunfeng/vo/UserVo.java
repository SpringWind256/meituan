package com.chunfeng.vo;

import com.chunfeng.entity.User;
import com.chunfeng.enums.UserSexEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * 用户视图层
 *
 * @author by 春风能解释
 * <p>
 * 2024/6/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(name = "用户视图")
public class UserVo extends User {
    /**
     * 序列化ID
     */
    @Serial
    private static final long serialVersionUID = 5326732999102549146L;
    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 性别名称
     */
    @Schema(description = "性别名称")
    private UserSexEnum sexName;
}
