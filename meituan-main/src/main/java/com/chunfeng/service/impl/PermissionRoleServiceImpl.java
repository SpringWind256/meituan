package com.chunfeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.entity.PermissionRole;
import com.chunfeng.mapper.PermissionRoleMapper;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.service.IPermissionRoleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限-角色业务层实现
 *
 * @author by 春风能解释
 * <p>
 * 2024/4/4
 */
@Service
public class PermissionRoleServiceImpl extends ServiceImpl<PermissionRoleMapper, PermissionRole> implements IPermissionRoleService {
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
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROLE_PERMISSION_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> bindCommonMethods(Map<Long, List<Long>> mapping, Integer index) {
        int columnTotal = 0;
        /////////////////////////////额外处理/////////////////////////////
        // 1.如果数据库中已经存在删除的数据,我们将其重新启用
        // 2.如果不存在再进行添加
        // 这样做的好处是
        // 数据库中的关系数据将始终不会因一次一次的删除而过度浪费
        // 能够最大程度减少数据库空间浪费
        /////////////////////////////////////////////////////////////////
        for (Map.Entry<Long, List<Long>> entry : mapping.entrySet()) {
            Long key = entry.getKey();
            List<Long> values = entry.getValue();
            // 将map映射为List<PermissionRole>集合方便数据库处理
            List<PermissionRole> rawPermissionRole = values.stream()
                    .map((value) -> new PermissionRole()
                            // 1.权限ID->[角色ID,角色ID,角色ID,....]
                            // 2.角色ID->[权限ID,权限ID,权限ID,....]
                            .setPermissionId(index == 2 ? value : key)// 权限ID
                            .setRoleId(index == 2 ? key : value)// 角色ID
                    )// 提取Map参数合并为新的对象集合
                    .collect(Collectors.toList());// 转换为集合
            // 根据权限ID与角色ID确定对应的唯一的关系数据
            List<PermissionRole> sumPermissionRoles = baseMapper.selectPermissionRoleByIds(rawPermissionRole);
            // 若能够在数据库中查到已有的数据,则直接跳出逻辑
            if (!sumPermissionRoles.isEmpty()) {
                throw new ServiceException(RequestException.INSERT_ERROR);
            }
            ///////////////////////////检查禁用的数据///////////////////////////
            // 检查已被禁用的关系数据
            List<PermissionRole> deletedPermissionRoles = baseMapper.selectPermissionRoleFromDeleted(rawPermissionRole);
            // 最终需要添加的数据
            // 若被禁用的部分不为空,证明数据库中有可以直接激活的关系数据
            if (!deletedPermissionRoles.isEmpty()) {
                // 将数据库中已有的ID重新启用以节省数据库空间
                Integer successColumn = baseMapper.enablePermissionRoleFromDeleted(deletedPermissionRoles);
                if (successColumn < 1) {
                    throw new ServiceException(RequestException.INSERT_ERROR);
                }
            }
            // 获得可以添加的ID
            List<PermissionRole> finalPermissionRoles = rawPermissionRole
                    .stream()
                    // 筛选满足条件的数据
                    .filter(finalId -> !deletedPermissionRoles.contains(finalId))// 根据ID取交集
                    .collect(Collectors.toList());
            ///////////////////////////添加额外的数据///////////////////////////
            // 若为空则直接跳过
            if (finalPermissionRoles.isEmpty()) {
                continue;
            }
            // 添加到数据库中
            Integer column = baseMapper.insertPermissionRole(finalPermissionRoles);
            if (column < 1) {
                throw new ServiceException(RequestException.INSERT_ERROR);
            }
            columnTotal += column;
        }
        return JsonRequest.success(columnTotal);
    }

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
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROLE_PERMISSION_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> unbindCommMethods(Map<Long, List<Long>> mapping, Integer index) {
        int columnTotal = 0;
        for (Map.Entry<Long, List<Long>> entry : mapping.entrySet()) {
            // 权限ID
            Long key = entry.getKey();
            // 角色ID
            List<Long> values = entry.getValue();
            // 将map映射为List<PermissionRole>集合方便数据库处理
            List<PermissionRole> collect = values.stream()
                    .map(value -> new PermissionRole()
                            // 1.权限ID->[角色ID,角色ID,角色ID,....]
                            // 2.角色ID->[权限ID,权限ID,权限ID,....]
                            .setPermissionId(index == 2 ? value : key)
                            .setRoleId(index == 2 ? key : value)
                    )// 提取Map参数合并为新的对象集合
                    .collect(Collectors.toList());// 转换为集合
            // 根据权限ID与角色ID确定对应的唯一的关系数据并统计个数
            List<PermissionRole> permissionRoles = baseMapper.selectPermissionRoleByIds(collect);
            // 若查出的数据库个数不等于提供的需要删除的数据个数,则我们判定数据库中已有数据被删除或提供的数据缺失
            if (permissionRoles.size() != collect.size()) {
                continue;
            }
            // 删除关系
            Integer column = baseMapper
                    .deletePermissionRole(collect);
            // 若删除失败
            if (column < 1) {
                throw new ServiceException(RequestException.DELETE_ERROR);
            }
            columnTotal += column;
        }
        return JsonRequest.success(columnTotal);
    }

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
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROLE_PERMISSION_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> deleteCommMethods(List<Long> ids, Integer index) {
        int columnTotal = 0;
        /////////////////////////删除权限-角色关系/////////////////////////
        List<PermissionRole> result = new ArrayList<>();
        // 根据权限查询
        if (index == 1) {
            result = baseMapper.selectPermissionRoleByPermission(ids);
        }
        // 根据路由查询
        else if (index == 2) {
            result = baseMapper.selectPermissionRoleByRole(ids);
        }
        // 提取ID
        List<Long> resultIds = result.stream()
                .map(PermissionRole::getId)
                .collect(Collectors.toList());
        // 删除相应的关系数据
        if (!result.isEmpty()) {
            Integer columnPr = baseMapper
                    .deletePermissionRoleById(resultIds);
            // 关系数据删除是否成功
            if (columnPr < 1) {
                throw new ServiceException(RequestException.DELETE_ERROR);
            }
            columnTotal += columnPr;
        }
        return JsonRequest.success(columnTotal);
    }

}
