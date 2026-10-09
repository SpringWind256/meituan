package com.chunfeng.controller;

import com.chunfeng.entity.Permission;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.service.IPermissionService;
import com.chunfeng.vo.PermissionVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限控制层
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/18
 */
@RestController
@RequestMapping("/per")
@Tag(name = "权限控制类", description = "权限操作访问接口")
public class PermissionController extends BaseController<IPermissionService, Permission> {
    /**
     * 查询所有权限信息
     *
     * @return JSON
     */
    @GetMapping
    @Operation(summary = "查询所有权限信息")
    public JsonRequest<List<PermissionVo>> lookAllPermission() {
        return baseService.lookAllPermission();
    }

    /**
     * 根据路由查询对应的权限列表
     *
     * @param routerId 路由ID
     * @return JSON
     */
    @GetMapping("/router")
    @Operation(summary = "根据路由ID查询对应的权限列表")
    public JsonRequest<List<Long>> lookPermissionsByRouter(
            @Parameter(description = "路由ID", required = true)
            @RequestParam Long routerId) {
        return baseService.lookPermissionsByRouter(routerId);
    }

    /**
     * 根据用户查询对应的权限列表
     *
     * @param userId 用户ID
     * @return JSON
     */
    @GetMapping("/user")
    @Operation(summary = "根据用户ID查询对应权限列表")
    public JsonRequest<List<PermissionVo>> lookPermissionByUser(
            @Parameter(description = "用户ID", required = true)
            @RequestParam Long userId) {
        return baseService.lookPermissionByUser(userId);
    }


    /**
     * 根据角色查询对应权限列表
     *
     * @param roleId 角色
     * @return JSON
     */
    @GetMapping("/role")
    @Operation(summary = "根据角色ID查询对应权限列表")
    public JsonRequest<List<Long>> lookPermissionByRole(
            @Parameter(description = "角色ID", required = true)
            @RequestParam Long roleId) {
        return baseService.lookPermissionByRole(roleId);
    }

    /**
     * 新增一条权限信息
     *
     * @param permission 权限信息
     * @return JSON
     */
    @PostMapping
    @Operation(summary = "新增一条权限信息")
    public JsonRequest<Integer> insertOnePermission(
            @Parameter(description = "权限信息")
            @RequestBody Permission permission) {
        return baseService.insertOnePermission(permission);
    }

    /**
     * 更新一条权限信息
     *
     * @param permission 权限信息
     * @return JSON
     */
    @PutMapping
    @Operation(summary = "更新一条权限信息")
    public JsonRequest<Integer> updateOnePermission(
            @Parameter(description = "权限信息", required = true)
            @RequestBody Permission permission) {
        return baseService.updateOnePermission(permission);
    }

    /**
     * 批量删除权限信息
     *
     * @param ids ID
     * @return JSON
     */
    @DeleteMapping
    @Operation(summary = "批量删除权限信息")
    public JsonRequest<Integer> deletePermissions(
            @Parameter(description = "ID", required = true)
            @RequestBody List<Long> ids) {
        return baseService.deletePermissions(ids);
    }

    /**
     * 绑定多个路由到单个权限
     *
     * @param mapping 映射条件(key为权限ID,value为>=1个路由ID)
     * @return JSON
     */
    @PostMapping("/bind")
    @Operation(summary = "绑定多个路由到单个权限")
    public JsonRequest<Integer> bindRouterToPermission(
            @Parameter(description = "映射条件(key为权限ID,value为>=1个路由ID)", required = true)
            @RequestBody Map<Long, List<Long>> mapping) {
        return baseService.bindRouterToPermission(mapping);
    }

    /**
     * 从权限中解绑不少于1个路由
     *
     * @param mapping 映射条件(key为权限,value为>=1个路由ID)
     * @return JSON
     */
    @DeleteMapping("/unbind")
    @Operation(summary = "从权限中解绑不少于1个路由")
    public JsonRequest<Integer> unbindRouterToPermission(
            @Parameter(description = "映射条件(key为权限ID,value为>=1个路由ID)", required = true)
            @RequestBody Map<Long, List<Long>> mapping) {
        return baseService.unbindRouterToPermission(mapping);
    }
}
