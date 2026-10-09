package com.chunfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chunfeng.entity.Role;
import com.chunfeng.result.JsonRequest;

import java.util.List;
import java.util.Map;

/**
 * 角色业务层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
public interface IRoleService extends IService<Role> {
    /**
     * 条件查询角色
     *
     * @param role 条件
     * @return JSON
     */
    JsonRequest<List<Role>> lookAllRole(Role role);

    /**
     * 条件查询角色
     *
     * @param current 页码
     * @param size    页长
     * @param role    条件
     * @return JSON
     */
    JsonRequest<List<Role>> lookRole(Integer current, Integer size, Role role);

    /**
     * 新增一个角色信息
     *
     * @param role 角色
     * @return JSON
     */
    JsonRequest<Integer> insertOneRole(Role role);

    /**
     * 修改一条角色信息
     *
     * @param role 角色
     * @return JSON
     */
    JsonRequest<Integer> updateOneRole(Role role);

    /**
     * 批量删除角色
     *
     * @param ids ID
     * @return JSON
     */
    JsonRequest<Integer> deleteRoles(List<Long> ids);

    /**
     * 绑定权限到角色
     *
     * @param mapping 映射条件(key为角色ID,value为不少于1的权限ID)
     * @return JSON
     */
    JsonRequest<Integer> bindPermissionToRole(Map<Long, List<Long>> mapping);

    /**
     * 从角色中解绑权限
     *
     * @param mapping 映射条件(key为角色ID,value为不少于1的权限ID)
     * @return JSON
     */
    JsonRequest<Integer> unbindPermissionToRole(Map<Long, List<Long>> mapping);
}
