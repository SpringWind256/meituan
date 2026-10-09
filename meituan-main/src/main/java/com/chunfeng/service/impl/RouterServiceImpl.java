package com.chunfeng.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.entity.Router;
import com.chunfeng.mapper.RouterMapper;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.service.IPermissionRouterService;
import com.chunfeng.service.IRouterService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 路由业务层实现
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Service
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements IRouterService {


    /**
     * 导入关系业务层
     */
    @Resource
    private IPermissionRouterService permissionRouterService;


    /**
     * 查询所有权限
     *
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.ROUTER_ARRAY)
    public JsonRequest<List<Router>> lookAllRouter() {
        return JsonRequest.success(baseMapper.selectAllRouter());
    }

    /**
     * 条件查询路由
     *
     * @param current 页码
     * @param size    页长
     * @param router  条件
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.ROUTER_ARRAY, key = "#root.args")
    public JsonRequest<List<Router>> lookAllRouter(Integer current, Integer size, Router router) {
        IPage<Router> routerPage = baseMapper
                .selectRouterPage(
                        new Page<>(current, size),
                        router);
        return JsonRequest.success(routerPage.getRecords(), routerPage.getTotal());
    }

    /**
     * 根据权限ID查询路由列表
     *
     * @param permissionId 权限ID
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.PERMISSION_ROUTER_ARRAY, key = "#root.args")
    public JsonRequest<List<Long>> lookRouterByPermission(Long permissionId) {
        return JsonRequest.success(baseMapper.selectRouterByPermission(permissionId));
    }

    /**
     * 根据用户ID查询路由列表
     *
     * @param userId 用户ID
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY, key = "#root.args")
    public JsonRequest<List<Router>> lookRouterByUserId(Long userId) {
        return JsonRequest.success(baseMapper.selectRouterByUser(userId));
    }

    /**
     * 添加一条路由信息
     *
     * @param router 路由信息
     * @return JSON
     */
    @Override
    @CacheEvict(value = CacheKeysConstants.UserKeys.ROUTER_ARRAY, allEntries = true)
    public JsonRequest<Integer> insertOneRouter(Router router) {
        // 判断路由是否存在
        Integer size = baseMapper.countRouterByCondition(router);
        if (size > 0) {
            throw new ServiceException(RequestException.DATA_EXISTS_ERROR);
        }
        int column = baseMapper.insert(router);
        if (column < 1) {
            throw new ServiceException(RequestException.INSERT_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 更新一条路由信息
     *
     * @param router 路由信息
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY}, allEntries = true)
    public JsonRequest<Integer> updateOneRouter(Router router) {
        Router router1 = baseMapper
                .selectById(router.getId());
        if (Objects.isNull(router1)) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        int column = baseMapper.updateById(router);
        if (column < 1) {
            throw new ServiceException(RequestException.UPDATE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 批量删除路由信息
     *
     * @param ids ID
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY}, allEntries = true)
    public JsonRequest<Integer> deleteRouter(List<Long> ids) {
        List<Router> routerList = baseMapper.selectBatchIds(ids);
        if (routerList.size() != ids.size()) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        // 删除本体
        int column = baseMapper.deleteByIds(ids);
        if (column < 1) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        // 删除权限关系数据
        JsonRequest<Integer> request = permissionRouterService.deleteCommMethods(ids, 2);
        if (!request.getSuccess()) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 绑定权限到路由
     *
     * @param mapping 映射条件(key代表路由ID,value代表权限ID)
     * @return JSON
     */
    @Override
    public JsonRequest<Integer> bindPermissionToRouter(Map<Long, List<Long>> mapping) {
        return permissionRouterService.bindCommonMethods(mapping, 2);
    }

    /**
     * 从路由中解绑权限
     *
     * @param mapping 映射条件(key代表路由ID,value代表权限ID)
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.ROUTER_ARRAY, CacheKeysConstants.UserKeys.PERMISSION_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN}, allEntries = true)
    public JsonRequest<Integer> unbindPermissionToRouter(Map<Long, List<Long>> mapping) {
        return permissionRouterService.unbindCommMethods(mapping, 2);
    }
}
