package com.chunfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chunfeng.dto.UserUpdatePasswordDto;
import com.chunfeng.dto.UserUpdatePhoneDto;
import com.chunfeng.entity.User;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.vo.UserVo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户表业务层接口
 *
 * @author 春风能解释
 * <p>
 * 2024-06-06
 */
public interface IUserService extends IService<User> {

    /**
     * 根据条件查询用户表信息
     *
     * @param current 页码
     * @param size    页长
     * @param user    用户表条件
     * @return JSON
     */
    JsonRequest<List<UserVo>> lookUser(Integer current, Integer size, User user);

    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return JSON
     */
    JsonRequest<String> login(String phone, String password);

    /**
     * 根据token查询当前用户信息
     *
     * @param token token
     * @return JSON
     */
    JsonRequest<UserVo> lookCurrentUser(String token);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return JSON
     */
    JsonRequest<Integer> register(User user);

    /**
     * 修改一条用户表信息
     *
     * @param user 用户表信息
     * @return JSON
     */
    JsonRequest<Integer> updateOneUser(User user);

    /**
     * 批量删除用户表信息
     *
     * @param ids 用户表ID列表
     * @return JSON
     */
    JsonRequest<Integer> deleteUsers(List<Long> ids);

    /**
     * 用户退出登录
     *
     * @param token token
     * @return JSON
     */
    JsonRequest<Boolean> logout(String token);

    /**
     * 获取手机验证码
     *
     * @param phone 手机号
     * @return JSON
     */
    JsonRequest<Boolean> getCode(String phone);

    /**
     * 校验手机验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return JSON
     */
    JsonRequest<Boolean> checkCode(String phone, String code);

    /**
     * 修改密码
     *
     * @param updatePasswordDto 信息
     * @return JSON
     */
    JsonRequest<Boolean> updatePassword(UserUpdatePasswordDto updatePasswordDto);

    /**
     * 修改手机号
     *
     * @param phoneDto 手机号信息
     * @return JSON
     */
    JsonRequest<Boolean> updatePhone(UserUpdatePhoneDto phoneDto);


    /**
     * 导出excel
     *
     * @param response 响应
     * @return JSON
     */
    JsonRequest<Void> exportExcel(HttpServletResponse response);

    /**
     * 上传头像
     *
     * @param id       用户ID
     * @param avatarId 头像ID
     * @return JSON
     */
    JsonRequest<Integer> uploadAvatar(Long id, Long avatarId);
}