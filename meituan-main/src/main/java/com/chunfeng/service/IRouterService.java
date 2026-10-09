package com.chunfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chunfeng.entity.Router;
import com.chunfeng.result.JsonRequest;

import java.util.List;
import java.util.Map;

/**
 * 路由业务层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
public interface IRouterService extends IService<Router> {
    /**
     * 查询所有权限
     *
     * @return JSON
     */
    JsonRequest<List<Router>> lookAllRouter();

    /**
     * 条件查询路由
     *
     * @param current 页码
     * @param size    页长
     * @param router  条件
     * @return JSON
     */
    JsonRequest<List<Router>> lookAllRouter(Integer current, Integer size, Router router);

    /**
     * 根据权限ID查询路由列表
     *
     * @param permissionId 权限ID
     * @return JSON
     */
    JsonRequest<List<Long>> lookRouterByPermission(Long permissionId);

    /**
     * 根据用户ID查询路由列表
     *
     * @param userId 用户ID
     * @return JSON
     */
    JsonRequest<List<Router>> lookRouterByUserId(Long userId);

    /**
     * 添加一条路由信息
     *
     * @param router 路由信息
     * @return JSON
     */
    JsonRequest<Integer> insertOneRouter(Router router);

    /**
     * 更新一条路由信息
     *
     * @param router 路由信息
     * @return JSON
     */
    JsonRequest<Integer> updateOneRouter(Router router);

    /**
     * 批量删除路由信息
     *
     * @param ids ID
     * @return JSON
     */
    JsonRequest<Integer> deleteRouter(List<Long> ids);

    /**
     * 绑定权限到路由
     *
     * @param mapping 映射条件(key代表路由ID,value代表权限ID)
     * @return JSON
     */
    JsonRequest<Integer> bindPermissionToRouter(Map<Long, List<Long>> mapping);

    /**
     * 从路由中解绑权限
     *
     * @param mapping 映射条件(key代表路由ID,value代表权限ID)
     * @return JSON
     */
    JsonRequest<Integer> unbindPermissionToRouter(Map<Long, List<Long>> mapping);
}
