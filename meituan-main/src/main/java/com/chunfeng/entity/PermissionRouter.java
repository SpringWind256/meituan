package com.chunfeng.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限-路由联表
 *
 * @author by 春风能解释
 * <p>
 * 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "权限-路由联表", description = "权限-路由联表")
public class PermissionRouter extends BaseEntity implements Serializable {
    /**
     * 序列化字段
     */
    @Serial
    private static final long serialVersionUID = -5364887397140565311L;
    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long permissionId;
    /**
     * 路由ID
     */
    @Schema(description = "路由ID")
    private Long routerId;
}