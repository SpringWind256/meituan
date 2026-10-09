package com.chunfeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.entity.PermissionRouter;
import com.chunfeng.mapper.PermissionRouterMapper;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.service.IPermissionRouterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限-路由业务层
 *
 * @author by 春风能解释
 * <p>
 * 2024/4/4
 */
@Service
public class PermissionRouterServiceImpl extends ServiceImpl<PermissionRouterMapper, PermissionRouter> implements IPermissionRouterService {
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
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.PERMISSION_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
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
            // 将map映射为List<PermissionRouter>集合方便数据库处理
            List<PermissionRouter> rawPermissionRouter = values.stream()
                    .map((value) -> new PermissionRouter()
                            // 1.权限ID->[路由ID,路由ID,路由ID,....]
                            // 2.路由ID->[权限ID,权限ID,权限ID,....]
                            .setPermissionId(index == 2 ? value : key)// 权限ID
                            .setRouterId(index == 2 ? key : value)// 路由ID
                    )// 提取Map参数合并为新的对象集合
                    .collect(Collectors.toList());// 转换为集合
            // 根据权限ID与路由ID确定对应的唯一的关系数据
            List<PermissionRouter> sumPermissionRouters = baseMapper.selectPermissionRouterByIds(rawPermissionRouter);
            // 若能够在数据库中查到已有的数据,则直接跳出逻辑
            if (!sumPermissionRouters.isEmpty()) {
                throw new ServiceException(RequestException.INSERT_ERROR);
            }
            ///////////////////////////检查禁用的数据///////////////////////////
            // 检查已被禁用的关系数据
            List<PermissionRouter> deletedPermissionRouters = baseMapper.selectPermissionRouterFromDeleted(rawPermissionRouter);
            // 最终需要添加的数据
            // 若被禁用的部分不为空,证明数据库中有可以直接激活的关系数据
            if (!deletedPermissionRouters.isEmpty()) {
                // 将数据库中已有的ID重新启用以节省数据库空间
                Integer successColumn = baseMapper.enablePermissionRouterFromDeleted(deletedPermissionRouters);
                if (successColumn < 1) {
                    throw new ServiceException(RequestException.INSERT_ERROR);
                }
            }
            // 获得可以添加的ID
            List<PermissionRouter> finalPermissionRouters = rawPermissionRouter
                    .stream()
                    // 筛选满足条件的数据
                    .filter(finalId -> !deletedPermissionRouters.contains(finalId))// 根据ID取交集
                    .collect(Collectors.toList());
            ///////////////////////////添加额外的数据///////////////////////////
            // 若为空则直接跳过
            if (finalPermissionRouters.isEmpty()) {
                continue;
            }
            // 添加到数据库中
            Integer column = baseMapper.insertPermissionRouter(finalPermissionRouters);
            if (column < 1) {
                throw new ServiceException(RequestException.INSERT_ERROR);
            }
            columnTotal += column;
        }
        return JsonRequest.success(columnTotal);
    }

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
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.PERMISSION_ARRAY, CacheKeysConstants.UserKeys.PERMISSION_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> unbindCommMethods(Map<Long, List<Long>> mapping, Integer index) {
        int columnTotal = 0;
        for (Map.Entry<Long, List<Long>> entry : mapping.entrySet()) {
            // 权限ID
            Long key = entry.getKey();
            // 路由ID
            List<Long> values = entry.getValue();
            // 将map映射为List<PermissionRouter>集合方便数据库处理
            List<PermissionRouter> collect = values.stream()
                    .map(value -> new PermissionRouter()
                            // 1.权限ID->[路由ID,路由ID,路由ID,....]
                            // 2.路由ID->[权限ID,权限ID,权限ID,....]
                            .setPermissionId(index == 2 ? value : key)
                            .setRouterId(index == 2 ? key : value)
                    )// 提取Map参数合并为新的对象集合
                    .collect(Collectors.toList());// 转换为集合
            // 根据权限ID与路由ID确定对应的唯一的关系数据并统计个数
            List<PermissionRouter> permissionRouters = baseMapper.selectPermissionRouterByIds(collect);
            // 若查出的数据库个数不等于提供的需要删除的数据个数,则我们判定数据库中已有数据被删除或提供的数据缺失
            if (permissionRouters.size() != collect.size()) {
                continue;
            }
            // 删除关系
            Integer column = baseMapper
                    .deletePermissionRouterByCondition(collect);
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
     * @param ids   ID(两种情况,要么根据权限ID删除,要么根据路由ID删除)
     * @param index 指数
     *              <ol>
     *              <li>权限ID</li>
     *              <li>路由ID</li>
     *              </ol>
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.PERMISSION_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN, CacheKeysConstants.UserKeys.USER_PERMISSION_ARRAY}, allEntries = true)
    public JsonRequest<Integer> deleteCommMethods(List<Long> ids, Integer index) {
        int columnTotal = 0;
        /////////////////////////删除权限-路由关系/////////////////////////
        List<Long> resultIds = new ArrayList<>();
        // 根据权限查询
        if (index == 1) {
            resultIds = baseMapper.selectPermissionRouterByPermission(ids);
        }
        // 根据路由查询
        else if (index == 2) {
            resultIds = baseMapper.selectPermissionRouterByRouter(ids);
        }
        // 删除相应的关系数据
        if (!resultIds.isEmpty()) {
            Integer columnPr = baseMapper
                    .deletePermissionRouterByIds(resultIds);
            // 关系数据删除是否成功
            if (columnPr < 1) {
                throw new ServiceException(RequestException.DELETE_ERROR);
            }
            columnTotal += columnPr;
        }
        return JsonRequest.success(columnTotal);
    }
}
