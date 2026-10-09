package com.chunfeng.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.entity.Role;
import com.chunfeng.mapper.RoleMapper;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.service.IPermissionRoleService;
import com.chunfeng.service.IRoleService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 角色业务层实现
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    /**
     * 权限-角色关系业务
     */
    @Resource
    private IPermissionRoleService permissionRoleService;

    /**
     * 条件查询角色
     *
     * @param role 条件
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.ROLE_ARRAY, key = "#root.args")
    public JsonRequest<List<Role>> lookAllRole(Role role) {
        return JsonRequest.success(baseMapper.selectRouter(role));
    }

    /**
     * 条件查询角色
     *
     * @param current 页码
     * @param size    页长
     * @param role    条件
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.ROLE_ARRAY, key = "#root.args")
    public JsonRequest<List<Role>> lookRole(Integer current, Integer size, Role role) {
        IPage<Role> rolePage = baseMapper.selectRouterPage(new Page<>(current, size), role);
        // 传入分页
        return JsonRequest.success(rolePage.getRecords(), rolePage.getTotal());
    }

    /**
     * 新增一个角色信息
     *
     * @param role 角色
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROLE_ARRAY}, allEntries = true)
    public JsonRequest<Integer> insertOneRole(Role role) {
        // 判断角色是否存在
        Integer size = baseMapper.countAllRoleByCondition(role);
        if (size > 0) {
            throw new ServiceException(RequestException.DATA_EXISTS_ERROR);
        }
        int column = baseMapper.insert(role);
        if (column < 1) {
            throw new ServiceException(RequestException.INSERT_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 修改一条角色信息
     *
     * @param role 角色
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROLE_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> updateOneRole(Role role) {
        if (role.getId() == 1 || role.getId() == 2) {
            return JsonRequest.error(RequestException.UPDATE_ERROR);
        }
        Role role1 = baseMapper.selectById(role.getId());
        if (Objects.isNull(role1)) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        // 判断待修改的角色信息是否已经存在
        Integer size = baseMapper.countAllRoleByCondition(role);
        if (size > 0) {
            throw new ServiceException(RequestException.DATA_EXISTS_ERROR);
        }
        int column = baseMapper.updateById(role);
        if (column < 1) {
            throw new ServiceException(RequestException.UPDATE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 批量删除角色
     *
     * @param ids ID
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROLE_ARRAY, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY}, allEntries = true)
    public JsonRequest<Integer> deleteRoles(List<Long> ids) {
        if (ids.contains(1L) || ids.contains(2L)) {
            return JsonRequest.error(RequestException.DELETE_ERROR);
        }
        List<Role> roles = baseMapper.selectBatchIds(ids);
        if (roles.size() != ids.size()) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        int column = baseMapper.deleteByIds(ids);
        if (column < 1) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        // 删除权限关系数据
        JsonRequest<Integer> jsonRequest = permissionRoleService.deleteCommMethods(ids, 2);
        if (!jsonRequest.getSuccess()) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 绑定权限到角色
     *
     * @param mapping 映射条件(key为角色ID,value为不少于1的权限ID)
     * @return JSON
     */
    @Override
    public JsonRequest<Integer> bindPermissionToRole(Map<Long, List<Long>> mapping) {
        return permissionRoleService.bindCommonMethods(mapping, 2);
    }

    /**
     * 从角色中解绑权限
     *
     * @param mapping 映射条件(key为角色ID,value为不少于1的权限ID)
     * @return JSON
     */
    @Override
    public JsonRequest<Integer> unbindPermissionToRole(Map<Long, List<Long>> mapping) {
        return permissionRoleService.unbindCommMethods(mapping, 2);
    }
}
