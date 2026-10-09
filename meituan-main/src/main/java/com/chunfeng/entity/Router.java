package com.chunfeng.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 路由表
 *
 * @author by 春风能解释
 * <p>
 * 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@Schema(name = "路由表", description = "路由表")
public class Router extends BaseEntity implements Serializable {
    /**
     * 序列化字段
     */
    @Serial
    private static final long serialVersionUID = -3061150993841498107L;
    /**
     * id
     */
    @Schema(description = "主键")
    @TableId
    private Long id;
    /**
     * 路由名
     */
    @Schema(description = "路由名")
    private String name;
    /**
     * 请求路径
     */
    @Schema(description = "请求路径")
    private String uri;
    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    private String method;
}