package com.chunfeng.entity;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基本的实体类
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/20
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "实体类")
public class BaseEntity implements Serializable {
    /**
     * 序列化字段
     */
    @Serial
    private static final long serialVersionUID = 6995320890624523042L;
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId
    private Long id;
    /**
     * 逻辑删除(0正常,1删除状态)
     */
    @Schema(description = "逻辑删除(0正常,1删除状态)")
    private Integer deleted;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;
    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
