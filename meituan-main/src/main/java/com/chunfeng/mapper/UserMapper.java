package com.chunfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chunfeng.entity.User;
import com.chunfeng.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表数据层接口
 *
 * @author 春风能解释
 * <p>
 * 2024-06-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 条件查询用户表
     *
     * @param user 用户表实体
     * @return 用户表列表
     */
    List<UserVo> selectUserList(@Param("user") User user);

    /**
     * 条件查询用户表
     *
     * @param page 分页条件
     * @param user 用户表实体
     * @return 用户表列表
     */
    IPage<UserVo> selectUserPages(@Param("page") IPage<UserVo> page, @Param("user") User user);

    /**
     * 查询用户表的详细信息
     *
     * @param id 用户id
     * @return 用户表的详细信息
     */
    UserVo selectUserVoById(Long id);

    /**
     * 按条件记录用户表的个数
     *
     * @param user 用户表条件
     * @return 个数
     */
    Integer countUserByCondition(User user);
}
