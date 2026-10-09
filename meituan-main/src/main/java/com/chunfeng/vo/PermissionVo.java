package com.chunfeng.vo;

import com.chunfeng.entity.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.List;

/**
 * 前端权限
 *
 * @author by 春风能解释
 * <p>
 * 2025/4/16
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "权限表", description = "权限表")
public class PermissionVo extends Permission {
    /**
     * 序列化ID
     */
    @Serial
    private static final long serialVersionUID = 6975888415517309630L;
    /**
     * 元数据
     */
    @Schema(description = "元数据")
    private Meta meta;
    /**
     * 子级权限
     */
    @Schema(description = "子级权限")
    private List<PermissionVo> children;

    /**
     * 设置元数据
     *
     * @param title 标题
     * @param icon  图标
     * @param affix 是否固定
     * @return this
     */
    public PermissionVo setMeta(String title, String icon, Boolean affix) {
        this.meta = new Meta()
                .setTitle(title)
                .setIcon(icon)
                .setAffix(affix);
        return this;
    }

    /**
     * 元数据
     */
    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @Schema(name = "元数据", description = "元数据")
    private static class Meta {
        /**
         * 标题
         */
        @Schema(description = "标题")
        private String title;
        /**
         * 图标
         */
        @Schema(description = "图标")
        private String icon;
        /**
         * 是否固定
         */
        @Schema(description = "是否固定")
        private Boolean affix;
    }
}
