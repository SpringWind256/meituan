package com.chunfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chunfeng.entity.Router;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 路由数据层接口
 *
 * @author by 春风能解释
 * <p>
 * 2024/3/16
 */
@Mapper
public interface RouterMapper extends BaseMapper<Router> {

    /**
     * 查询所有路由信息
     *
     * @return 列表
     */
    List<Router> selectAllRouter();

    /**
     * 分页查询路由
     *
     * @param page   分页
     * @param router 条件
     * @return 分页列表
     */
    IPage<Router> selectRouterPage(IPage<Router> page, @Param("router") Router router);

    /**
     * 根据权限ID查询路由
     *
     * @param perId 权限ID
     * @return 路由列表
     */
    List<Long> selectRouterByPermission(@Param("perId") Long perId);

    /**
     * 根据角色查询路由信息
     *
     * @param roleId 角色
     * @return 路由列表
     */
    List<Long> selectRouterByRole(@Param("roleId") Long roleId);

    /**
     * 根据用户查询路由
     *
     * @param userId 用户ID
     * @return 路由列表
     */
    List<Router> selectRouterByUser(@Param("userId") Long userId);

    /**
     * 按条件记录路由的个数
     *
     * @param router 路由条件
     * @return 个数
     */
    Integer countRouterByCondition(@Param("router") Router router);
}
