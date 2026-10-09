package com.chunfeng.controller;

import com.chunfeng.entity.Router;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.service.IRouterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 路由控制层
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/18
 */
@RestController
@RequestMapping("/router")
@Tag(name = "路由控制类", description = "路由操作访问接口")
public class RouterController extends BaseController<IRouterService, Router> {
    /**
     * 查询所有路由
     *
     * @return JSON
     */
    @GetMapping
    @Operation(summary = "查询所有路由")
    public JsonRequest<List<Router>> lookAllRouter() {
        return baseService.lookAllRouter();
    }

    /**
     * 条件查询路由
     *
     * @param current 页码
     * @param size    页长
     * @param router  条件
     * @return JSON
     */
    @GetMapping("/list")
    @Operation(summary = "条件查询路由")
    public JsonRequest<List<Router>> lookAllRouter(
            @Parameter(description = "页码", required = true)
            @RequestParam Integer current,
            @Parameter(description = "页长", required = true)
            @RequestParam Integer size,
            @Parameter(description = "条件")
            Router router) {
        return baseService.lookAllRouter(current, size, router);
    }

    /**
     * 根据权限ID查询路由列表
     *
     * @param permissionId 权限ID
     * @return JSON
     */
    @GetMapping("/per")
    @Operation(summary = "根据权限ID查询路由列表")
    public JsonRequest<List<Long>> lookRouterByPermission(
            @Parameter(description = "权限ID", required = true)
            @RequestParam Long permissionId) {
        return baseService.lookRouterByPermission(permissionId);
    }

    /**
     * 添加一条路由信息
     *
     * @param router 路由信息
     * @return JSON
     */
    @PostMapping
    @Operation(summary = "添加一条路由信息")
    public JsonRequest<Integer> insertOneRouter(
            @Parameter(description = "路由信息", required = true)
            @RequestBody Router router) {
        return baseService.insertOneRouter(router);
    }

    /**
     * 更新一条路由信息
     *
     * @param router 路由信息
     * @return JSON
     */
    @PutMapping
    @Operation(summary = "更新一条权限信息")
    public JsonRequest<Integer> updateOneRouter(
            @Parameter(description = "路由信息", required = true)
            @RequestBody Router router) {
        return baseService.updateOneRouter(router);
    }

    /**
     * 批量删除路由信息
     *
     * @param ids ID
     * @return JSON
     */
    @DeleteMapping
    @Operation(summary = "批量删除权限信息")
    public JsonRequest<Integer> deleteRouter(
            @Parameter(description = "ID", required = true)
            @RequestBody List<Long> ids) {
        return baseService.deleteRouter(ids);
    }

    /**
     * 绑定权限到路由
     *
     * @param mapping 映射条件(key代表路由ID,value代表权限ID)
     * @return JSON
     */
    @PostMapping("/bind")
    @Operation(summary = "绑定权限到路由")
    public JsonRequest<Integer> bindPermissionToRouter(
            @Parameter(description = "映射条件(key代表路由ID,value代表权限ID)", required = true)
            @RequestBody Map<Long, List<Long>> mapping) {
        return baseService.bindPermissionToRouter(mapping);
    }

    /**
     * 从路由中解绑权限
     *
     * @param mapping 映射条件(key代表路由ID,value代表权限ID)
     * @return JSON
     */
    @DeleteMapping("/unbind")
    @Operation(summary = "从路由中解绑权限")
    public JsonRequest<Integer> unbindPermissionToRouter(
            @Parameter(description = "映射条件(key代表路由ID,value代表权限ID)", required = true)
            @RequestBody Map<Long, List<Long>> mapping) {
        return baseService.unbindPermissionToRouter(mapping);
    }
}
