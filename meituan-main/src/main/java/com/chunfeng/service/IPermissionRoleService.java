package com.chunfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chunfeng.entity.PermissionRole;
import com.chunfeng.result.JsonRequest;

import java.util.List;
import java.util.Map;

/**
 * 权限-角色业务层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/4/4
 */
public interface IPermissionRoleService extends IService<PermissionRole> {


    /**
     * 绑定权限-角色的公共方法
     *
     * @param mapping 映射(两种情况,key为权限ID或key为角色ID,value为集合与key值对立)
     * @param index   指数
     *                <ol>
     *                <li>权限ID->[角色ID,角色ID,角色ID,....]</li>
     *                <li>角色ID->[权限ID,权限ID,权限ID,....]</li>
     *                </ol>
     * @return JSON
     */
    JsonRequest<Integer> bindCommonMethods(Map<Long, List<Long>> mapping, Integer index);

    /**
     * 解绑权限-角色的公共方法
     *
     * @param mapping 映射(两种情况,key为权限ID或key为角色ID,value为集合与key值对立)
     * @param index   指数
     *                <ol>
     *                <li>权限ID->[角色ID,角色ID,角色ID,....]</li>
     *                <li>角色ID->[权限ID,权限ID,权限ID,....]</li>
     *                </ol>
     * @return JSON
     */
    JsonRequest<Integer> unbindCommMethods(Map<Long, List<Long>> mapping, Integer index);

    /**
     * 删除关系数据的公共方法
     *
     * @param ids   ID(两种情况,要么根据权限ID删除,要么根据角色ID删除)
     * @param index 指数
     *              <ol>
     *              <li>权限ID</li>
     *              <li>角色ID</li>
     *              </ol>
     * @return JSON
     */
    JsonRequest<Integer> deleteCommMethods(List<Long> ids, Integer index);
}
