package com.chunfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chunfeng.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限数据层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 查询所有权限
     *
     * @return 数据
     */
    List<Permission> selectAllPermission();

    /**
     * 条件查询权限
     *
     * @param page       分页
     * @param permission 条件
     * @return 分页数据
     */
    IPage<Permission> selectPermissionPage(@Param("page") IPage<Permission> page, @Param("permission") Permission permission);

    /**
     * 根据路由条件查询权限列表
     *
     * @param routerId 路由ID
     * @return 权限列表
     */
    List<Long> selectPermissionByRouter(Long routerId);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Long> selectPermissionByRole(Long roleId);

    /**
     * 根据用户查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> selectPermissionByUser(Long userId);

    /**
     * 条件记录权限个数
     *
     * @param permission 条件
     * @return 权限列表
     */
    Integer countPermissionByCondition(@Param("per") Permission permission);
}
