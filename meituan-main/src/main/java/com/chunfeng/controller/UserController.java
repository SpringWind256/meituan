package com.chunfeng.controller;

import com.chunfeng.contants.UserConstants;
import com.chunfeng.dto.UserUpdatePasswordDto;
import com.chunfeng.dto.UserUpdatePhoneDto;
import com.chunfeng.entity.User;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.service.IUserService;
import com.chunfeng.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表业务层实现
 *
 * @author 春风能解释
 * <p>
 * 2024-06-05
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户表控制类", description = "用户表操作访问接口")
public class UserController extends BaseController<IUserService, User> {

    /**
     * 导出用户信息
     *
     * @param response 响应
     * @return JSON
     */
    @PostMapping("/export")
    @Operation(summary = "导出用户信息")
    public JsonRequest<Void> exportExcel(HttpServletResponse response) {
        return baseService.exportExcel(response);
    }

    /**
     * 根据条件查询用户表信息
     *
     * @param current 页码
     * @param size    页长
     * @param user    条件
     * @return JSON
     */
    @GetMapping("/list")
    @Operation(summary = "根据条件查询用户表信息")
    public JsonRequest<List<UserVo>> lookUser(
            @Parameter(description = "页码", required = true)
            @RequestParam Integer current,
            @Parameter(description = "页长", required = true)
            @RequestParam Integer size,
            @Parameter(description = "用户表条件", required = true)
            User user) {
        return baseService.lookUser(current, size, user);
    }


    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return JSON
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    public JsonRequest<String> login(
            @Parameter(description = "手机号", required = true)
            @RequestParam String phone,
            @Parameter(description = "密码", required = true)
            @RequestParam String password) {
        return baseService.login(phone, password);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return JSON
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public JsonRequest<Integer> register(User user) {
        return baseService.register(user);
    }

    /**
     * 修改一条用户表信息
     *
     * @param user 用户表信息
     * @return JSON
     */
    @PutMapping
    @Operation(summary = "修改一条用户表信息")
    public JsonRequest<Integer> updateOneUser(
            @Parameter(description = "用户表信息", required = true)
            @RequestBody User user) {
        return baseService.updateOneUser(user);
    }

    /**
     * 批量删除用户表信息
     *
     * @param ids 用户表id列表
     * @return JSON
     */
    @DeleteMapping
    @Operation(summary = "批量删除用户表信息")
    public JsonRequest<Integer> deleteUsers(
            @Parameter(description = "用户表id列表", required = true)
            @RequestBody List<Long> ids) {
        return baseService.deleteUsers(ids);
    }

    /**
     * 根据token查询当前用户信息
     *
     * @param token token
     * @return JSON
     */
    @GetMapping("/info")
    @Operation(summary = "根据token查询当前用户信息")
    public JsonRequest<UserVo> lookCurrentUser(
            @Parameter(description = UserConstants.AUTHORIZATION, required = true)
            @RequestParam String token) {
        return baseService.lookCurrentUser(token);
    }

    /**
     * 用户退出登录
     *
     * @param token token
     * @return JSON
     */
    @PostMapping("/logout")
    @Operation(summary = "用户退出登录")
    public JsonRequest<Boolean> logout(@RequestParam String token) {
        return baseService.logout(token);
    }

    /**
     * 获取手机验证码
     *
     * @param phone 手机号
     * @return JSON
     */
    @GetMapping("/phoneCode")
    @Operation(summary = "获取手机验证码")
    public JsonRequest<Boolean> getCode(
            @Parameter(description = "手机号", required = true)
            @RequestParam String phone) {
        return baseService.getCode(phone);
    }

    /**
     * 修改密码
     *
     * @param updatePasswordDto 信息
     * @return JSON
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public JsonRequest<Boolean> updatePassword(
            @Parameter(description = "修改密码信息", required = true)
            @RequestBody UserUpdatePasswordDto updatePasswordDto) {
        return baseService.updatePassword(updatePasswordDto);
    }

    /**
     * 校验手机验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return JSON
     */
    @PostMapping("/checkPhoneCode")
    @Operation(summary = "获取手机验证码")
    public JsonRequest<Boolean> checkCode(
            @Parameter(description = "手机号", required = true)
            @RequestParam String phone,
            @Parameter(description = "验证码", required = true)
            @RequestParam String code) {
        return baseService.checkCode(phone, code);
    }

    /**
     * 修改手机号
     *
     * @param phoneDto 手机号信息
     * @return JSON
     */
    @PutMapping("/phone")
    @Operation(summary = "修改手机号信息")
    public JsonRequest<Boolean> updatePhone(
            @Parameter(description = "修改手机号信息", required = true)
            @RequestBody UserUpdatePhoneDto phoneDto) {
        return baseService.updatePhone(phoneDto);
    }

    /**
     * 上传头像
     *
     * @param userId   用户ID
     * @param avatarId 头像ID
     * @return JSON
     */
    @PostMapping("/avatar")
    @Operation(summary = "上传头像")
    public JsonRequest<Integer> uploadAvatar(
            @Parameter(description = "用户ID", required = true)
            @RequestParam Long userId,
            @Parameter(description = "头像ID", required = true)
            @RequestParam Long avatarId) {
        return baseService.uploadAvatar(userId, avatarId);
    }
}