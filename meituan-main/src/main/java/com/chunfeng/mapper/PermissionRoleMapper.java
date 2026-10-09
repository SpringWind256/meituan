package com.chunfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chunfeng.entity.PermissionRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限-角色数据层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Mapper
public interface PermissionRoleMapper extends BaseMapper<PermissionRole> {

    /**
     * 根据权限ID查询关系数据
     *
     * @param ids ID
     * @return 关系列表
     */
    List<PermissionRole> selectPermissionRoleByPermission(@Param("ids") List<Long> ids);

    /**
     * 根据角色ID查询关系数据
     *
     * @param ids ID
     * @return 关系列表
     */
    List<PermissionRole> selectPermissionRoleByRole(@Param("ids") List<Long> ids);

    /**
     * 查询已删除的关系数据
     *
     * @param permissionRoles 关系列表
     * @return 列表
     */
    List<PermissionRole> selectPermissionRoleFromDeleted(@Param("prs") List<PermissionRole> permissionRoles);

    /**
     * 根据权限与角色ID确定关系列表
     *
     * @param permissionRoles 关系数据
     * @return 列表
     */
    List<PermissionRole> selectPermissionRoleByIds(@Param("prs") List<PermissionRole> permissionRoles);

    /**
     * 批量新增关系数据
     *
     * @param permissionRoles 关系数据
     * @return 影响行数
     */
    Integer insertPermissionRole(@Param("prs") List<PermissionRole> permissionRoles);

    /**
     * 重新激活已被删除的关系数据
     *
     * @param permissionRoles 关系列表
     * @return 影响行数
     */
    Integer enablePermissionRoleFromDeleted(@Param("prs") List<PermissionRole> permissionRoles);

    /**
     * 批量删除关系数据
     *
     * @param permissionRoles 关系数据
     * @return 影响行数
     */
    Integer deletePermissionRole(@Param("prs") List<PermissionRole> permissionRoles);

    /**
     * 批量删除关系数据
     *
     * @param ids ID
     * @return 影响行数
     */
    Integer deletePermissionRoleById(@Param("ids") List<Long> ids);
}
