package com.chunfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chunfeng.entity.PermissionRouter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限-路由数据层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Mapper
public interface PermissionRouterMapper extends BaseMapper<PermissionRouter> {
    /**
     * 根据路由ID查询绑定的关系数据
     *
     * @param ids 路由ID
     * @return ID
     */
    List<Long> selectPermissionRouterByRouter(@Param("ids") List<Long> ids);

    /**
     * 根据权限ID查询绑定的关系数据
     *
     * @param ids 权限ID
     * @return ID
     */
    List<Long> selectPermissionRouterByPermission(@Param("ids") List<Long> ids);

    /**
     * 记录关系数据中已经失效的数据
     *
     * @param permissionRouters 关系集合
     * @return 个数
     */
    List<PermissionRouter> selectPermissionRouterFromDeleted(@Param("prs") List<PermissionRouter> permissionRouters);

    /**
     * 重新激活已被删除的关系数据
     *
     * @param permissionRouters 关系列表
     * @return 影响行数
     */
    Integer enablePermissionRouterFromDeleted(@Param("prs") List<PermissionRouter> permissionRouters);

    /**
     * 绑定关系信息
     *
     * @param permissionRouters 映射
     * @return 影响行数
     */
    Integer insertPermissionRouter(@Param("prs") List<PermissionRouter> permissionRouters);

    /**
     * 根据关系批量解绑关系信息
     *
     * @param permissionRouters 映射
     * @return 影响行数
     */
    Integer deletePermissionRouterByCondition(@Param("prs") List<PermissionRouter> permissionRouters);


    /**
     * 根据ID批量解绑关系信息
     *
     * @param ids ID
     * @return 影响行数
     */
    Integer deletePermissionRouterByIds(@Param("ids") List<Long> ids);

    /**
     * 根据权限与路由ID查询关系数据
     *
     * @param permissionRouters 关系集合
     * @return 个数
     */
    List<PermissionRouter> selectPermissionRouterByIds(@Param("prs") List<PermissionRouter> permissionRouters);
}
