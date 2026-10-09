package com.chunfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chunfeng.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色数据层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 分页查询
     *
     * @param role 条件
     * @return 列表
     */
    List<Role> selectRouter(@Param("role") Role role);

    /**
     * 分页查询
     *
     * @param page 分页插件
     * @param role 条件
     * @return 列表
     */
    IPage<Role> selectRouterPage(@Param("page") IPage<Role> page, @Param("role") Role role);

    /**
     * 按条件记录角色的个数
     *
     * @param role 条件
     * @return 个数
     */
    Integer countAllRoleByCondition(@Param("role") Role role);
}
