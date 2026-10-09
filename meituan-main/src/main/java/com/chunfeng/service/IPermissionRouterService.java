package com.chunfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chunfeng.entity.PermissionRouter;
import com.chunfeng.result.JsonRequest;

import java.util.List;
import java.util.Map;

/**
 * 权限-路由业务层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/4/4
 */
public interface IPermissionRouterService extends IService<PermissionRouter> {
    /**
     * 绑定权限-路由的公共方法
     *
     * @param mapping 映射(两种情况,key为权限ID或key为路由ID,value为集合与key值对立)
     * @param index   指数
     *                <ol>
     *                <li>权限ID->[路由ID,路由ID,路由ID,....]</li>
     *                <li>路由ID->[权限ID,权限ID,权限ID,....]</li>
     *                </ol>
     * @return JSON
     */
    JsonRequest<Integer> bindCommonMethods(Map<Long, List<Long>> mapping, Integer index);

    /**
     * 解绑权限-路由的公共方法
     *
     * @param mapping 映射(两种情况,key为权限ID或key为路由ID,value为集合与key值对立)
     * @param index   指数
     *                <ol>
     *                <li>权限ID->[路由ID,路由ID,路由ID,....]</li>
     *                <li>路由ID->[权限ID,权限ID,权限ID,....]</li>
     *                </ol>
     * @return JSON
     */
    JsonRequest<Integer> unbindCommMethods(Map<Long, List<Long>> mapping, Integer index);

    /**
     * 删除关系数据的公共方法
     *
     * @param ids   ID(两种情况,要么根据权限ID删除,要么根据路由ID删除)
     * @param index 指数
     *              <ol>
     *              <li>权限ID</li>
     *              <li>路由ID</li>
     *              </ol>
     * @return JSON
     */
    JsonRequest<Integer> deleteCommMethods(List<Long> ids, Integer index);
}
