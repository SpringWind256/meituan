package com.chunfeng.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chunfeng.entity.Permission;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.vo.PermissionVo;

import java.util.List;
import java.util.Map;

/**
 * 权限业务层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
public interface IPermissionService extends IService<Permission> {

    /**
     * 查询所有权限信息
     *
     * @return JSON
     */
    JsonRequest<List<PermissionVo>> lookAllPermission();

    /**
     * 根据路由查询对应的权限列表
     *
     * @param routerId 路由ID
     * @return JSON
     */
    JsonRequest<List<Long>> lookPermissionsByRouter(Long routerId);

    /**
     * 根据用户查询对应的权限列表
     *
     * @param userId 用户ID
     * @return JSON
     */
    JsonRequest<List<PermissionVo>> lookPermissionByUser(Long userId);

    /**
     * 根据角色查询对应权限列表
     *
     * @param roleId 角色ID
     * @return JSON
     */
    JsonRequest<List<Long>> lookPermissionByRole(Long roleId);

    /**
     * 新增一条权限信息
     *
     * @param permission 权限信息
     * @return JSON
     */
    JsonRequest<Integer> insertOnePermission(Permission permission);

    /**
     * 更新一条权限信息
     *
     * @param permission 权限信息
     * @return JSON
     */
    JsonRequest<Integer> updateOnePermission(Permission permission);

    /**
     * 批量删除权限信息
     *
     * @param ids ID
     * @return JSON
     */
    JsonRequest<Integer> deletePermissions(List<Long> ids);

    /**
     * 绑定多个路由到单个权限
     *
     * @param mapping 映射条件(key为权限ID,value为>=1个路由ID)
     * @return JSON
     */
    JsonRequest<Integer> bindRouterToPermission(Map<Long, List<Long>> mapping);

    /**
     * 从权限中解绑不少于1个路由
     *
     * @param mapping 映射条件(key为权限,value为>=1个路由ID)
     * @return JSON
     */
    JsonRequest<Integer> unbindRouterToPermission(Map<Long, List<Long>> mapping);

}
