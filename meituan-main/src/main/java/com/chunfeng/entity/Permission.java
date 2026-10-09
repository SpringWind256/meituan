package com.chunfeng.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限表
 *
 * @author by 春风能解释
 * <p>
 * 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "权限表", description = "权限表")
public class Permission extends BaseEntity implements Serializable {
    /**
     * 序列化字段
     */
    @Serial
    private static final long serialVersionUID = 2139601223224865058L;
    /**
     * 权限名
     */
    @Schema(description = "权限名")
    private String name;
    /**
     * 前端路径
     */
    @Schema(description = "前端路径")
    private String path;
    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String note;
    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;
    /**
     * 组件
     */
    @Schema(description = "组件")
    private String component;
    /**
     * 重定向
     */
    @Schema(description = "重定向")
    private String redirect;
    /**
     * 是否固定
     */
    @Schema(description = "是否固定")
    private Boolean affix;
    /**
     * 是否隐藏
     */
    @Schema(description = "是否隐藏")
    private Boolean hidden;
    /**
     * 父级id
     */
    @Schema(description = "父级id")
    private Long parentId;
    /**
     * 排序
     */
    @Schema(description = "排序")
    @TableField("`order`")
    private Integer order;
}