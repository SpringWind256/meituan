package com.chunfeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.entity.Permission;
import com.chunfeng.mapper.PermissionMapper;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.service.IPermissionRoleService;
import com.chunfeng.service.IPermissionRouterService;
import com.chunfeng.service.IPermissionService;
import com.chunfeng.utils.TreeUtils;
import com.chunfeng.vo.PermissionVo;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 权限业务层实现
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {


    /**
     * 关系业务层
     */
    @Resource
    private IPermissionRouterService permissionRouterService;

    /**
     * 导入关系业务层
     */
    @Resource
    private IPermissionRoleService permissionRoleService;

    /**
     * 查询所有权限信息
     *
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.PERMISSION_ARRAY)
    public JsonRequest<List<PermissionVo>> lookAllPermission() {
        List<Permission> permissions = baseMapper.selectAllPermission();
        // 将权限信息转换为VO
        List<PermissionVo> permissionVos = permissions
                .stream()
                .map(v -> {
                    PermissionVo vo = new PermissionVo();
                    BeanUtil.copyProperties(v, vo);
                    return vo.setMeta(v.getTitle(), v.getIcon(), v.getAffix());
                })
                .toList();
        // 构建树形结构
        List<PermissionVo> permissionVos1 = TreeUtils.buildTree(permissionVos,
                -1L,
                PermissionVo::getId,
                PermissionVo::getParentId,
                PermissionVo::setChildren);
        return JsonRequest.success(permissionVos1);
    }

    /**
     * 根据路由查询对应的权限列表
     *
     * @param routerId 路由ID
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.PERMISSION_ROUTER_ARRAY, key = "#root.args")
    public JsonRequest<List<Long>> lookPermissionsByRouter(Long routerId) {
        // 查询权限数据
        return JsonRequest.success(baseMapper.selectPermissionByRouter(routerId));
    }

    /**
     * 根据用户查询对应的权限列表
     *
     * @param userId 用户ID
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY, key = "#root.args")
    public JsonRequest<List<PermissionVo>> lookPermissionByUser(Long userId) {
        List<Permission> permissions = baseMapper.selectPermissionByUser(userId);
        // 将权限信息转换为VO
        List<PermissionVo> permissionVos = permissions
                .stream()
                .map(v -> {
                    PermissionVo vo = new PermissionVo();
                    BeanUtil.copyProperties(v, vo);
                    return vo.setMeta(v.getTitle(), v.getIcon(), v.getAffix());
                })
                .toList();
        // 构建树形结构
        List<PermissionVo> permissionVos1 = TreeUtils.buildTree(permissionVos,
                -1L,
                PermissionVo::getId,
                PermissionVo::getParentId,
                PermissionVo::setChildren);
        return JsonRequest.success(permissionVos1);
    }

    /**
     * 根据角色查询对应权限列表
     *
     * @param roleId 角色ID
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.ROLE_PERMISSION_ARRAY, key = "#root.args")
    public JsonRequest<List<Long>> lookPermissionByRole(Long roleId) {
        return JsonRequest.success(baseMapper.selectPermissionByRole(roleId));
    }

    /**
     * 新增一条权限信息
     *
     * @param permission 权限信息
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> insertOnePermission(Permission permission) {
        Integer size = baseMapper.countPermissionByCondition(permission);
        // 判断权限是否已经存在
        if (size > 0) {
            throw new ServiceException(RequestException.DATA_EXISTS_ERROR);
        }
        // 添加权限
        int column = baseMapper.insert(permission);
        if (column < 1) {
            throw new ServiceException(RequestException.INSERT_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 更新一条权限信息
     *
     * @param permission 权限信息
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.PERMISSION_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> updateOnePermission(Permission permission) {
        Permission permission1 = baseMapper.selectById(permission.getId());
        if (Objects.isNull(permission1)) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        int column = baseMapper.updateById(permission);
        if (column < 1) {
            throw new ServiceException(RequestException.UPDATE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 批量删除权限信息
     *
     * @param ids ID
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.PERMISSION_ARRAY, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> deletePermissions(List<Long> ids) {
        List<Permission> permissions = baseMapper.selectBatchIds(ids);
        if (permissions.size() != ids.size()) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        // 删除本体数据
        int column = baseMapper.deleteByIds(ids);
        if (column < 1) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        // 删除路由关系数据
        JsonRequest<Integer> request = permissionRouterService.deleteCommMethods(ids, 1);
        if (!request.getSuccess()) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        // 删除角色关系数据
        JsonRequest<Integer> request1 = permissionRoleService.deleteCommMethods(ids, 2);
        if (!request1.getSuccess()) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 绑定多个路由到单个权限
     *
     * @param mapping 映射条件(key为权限ID,value为>=1个路由ID)
     * @return JSON
     */
    @Override
    public JsonRequest<Integer> bindRouterToPermission(Map<Long, List<Long>> mapping) {
        return permissionRouterService.bindCommonMethods(mapping, 1);
    }

    /**
     * 从权限中解绑不少于1个路由
     *
     * @param mapping 映射条件(key为权限,value为>=1个路由ID)
     * @return JSON
     */
    @Override
    public JsonRequest<Integer> unbindRouterToPermission(Map<Long, List<Long>> mapping) {
        return permissionRouterService.unbindCommMethods(mapping, 1);
    }


}
