package com.chunfeng.controller;

import com.chunfeng.entity.Role;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色控制层
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/18
 */
@RestController
@RequestMapping("/role")
@Tag(name = "角色控制类", description = "角色操作访问接口")
public class RoleController extends BaseController<IRoleService, Role> {
    /**
     * 条件查询角色
     *
     * @param role 条件
     * @return JSON
     */
    @GetMapping("/listAll")
    @Operation(summary = "条件查询角色")
    public JsonRequest<List<Role>> lookRole(
            @Parameter(description = "条件")
            Role role) {
        return baseService.lookAllRole(role);
    }

    /**
     * 条件查询角色
     *
     * @param current 页码
     * @param size    页长
     * @param role    条件
     * @return JSON
     */
    @GetMapping("/list")
    @Operation(summary = "条件查询角色")
    public JsonRequest<List<Role>> lookRole(
            @Parameter(description = "页码", required = true)
            @RequestParam Integer current,
            @Parameter(description = "页长", required = true)
            @RequestParam Integer size,
            @Parameter(description = "条件")
            Role role) {
        return baseService.lookRole(current, size, role);
    }

    /**
     * 新增一个角色信息
     *
     * @param role 角色
     * @return JSON
     */
    @PostMapping
    @Operation(summary = "新增一个角色信息")
    public JsonRequest<Integer> insertOneRole(
            @Parameter(description = "角色", required = true)
            @RequestBody Role role) {
        return baseService.insertOneRole(role);
    }

    /**
     * 修改一条角色信息
     *
     * @param role 角色
     * @return JSON
     */
    @PutMapping
    @Operation(summary = "修改一条角色信息")
    public JsonRequest<Integer> updateOneRole(
            @Parameter(description = "角色", required = true)
            @RequestBody Role role) {
        return baseService.updateOneRole(role);
    }

    /**
     * 批量删除角色
     *
     * @param ids ID
     * @return JSON
     */
    @DeleteMapping
    @Operation(summary = "批量删除角色")
    public JsonRequest<Integer> deleteRoles(
            @Parameter(description = "ID", required = true)
            @RequestBody List<Long> ids) {
        return baseService.deleteRoles(ids);
    }

    /**
     * 绑定权限到角色
     *
     * @param mapping 映射条件(key为角色ID,value为不少于1的权限ID)
     * @return JSON
     */
    @PostMapping("/bind")
    @Operation(summary = "绑定权限到角色")
    public JsonRequest<Integer> bindPermissionToRole(
            @Parameter(description = "映射条件(key为角色ID,value为不少于1的权限ID)", required = true)
            @RequestBody Map<Long, List<Long>> mapping) {
        return baseService.bindPermissionToRole(mapping);
    }

    /**
     * 从角色中解绑权限
     *
     * @param mapping 映射条件(key为角色ID,value为不少于1的权限ID)
     * @return JSON
     */

    @DeleteMapping("/unbind")
    @Operation(summary = "从角色中解绑权限")
    public JsonRequest<Integer> unbindPermissionToRole(
            @Parameter(description = "映射条件(key为角色ID,value为不少于1的权限ID)", required = true)
            @RequestBody Map<Long, List<Long>> mapping) {
        return baseService.unbindPermissionToRole(mapping);
    }

}
