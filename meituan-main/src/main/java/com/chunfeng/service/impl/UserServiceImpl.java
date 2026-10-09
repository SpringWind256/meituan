package com.chunfeng.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chunfeng.contants.CacheKeysConstants;
import com.chunfeng.contants.UserConstants;
import com.chunfeng.dto.UserUpdatePasswordDto;
import com.chunfeng.dto.UserUpdatePhoneDto;
import com.chunfeng.entity.Router;
import com.chunfeng.entity.User;
import com.chunfeng.enums.UserStatusEnum;
import com.chunfeng.mapper.UserMapper;
import com.chunfeng.note.ExcludeMethods;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import com.chunfeng.service.IRouterService;
import com.chunfeng.service.IUserService;
import com.chunfeng.utils.ExcelUtils;
import com.chunfeng.utils.RedisClientsUtils;
import com.chunfeng.utils.TokenUtils;
import com.chunfeng.vo.UserVo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 用户表业务层实现
 *
 * @author 春风能解释
 * <p>
 * 2024-06-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    /**
     * 导入redis工具类
     */
    private final RedisClientsUtils redisClientsUtils;

    /**
     * 导入密码加密器
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 导入路由服务
     */
    private final IRouterService routerService;

    /**
     * 根据条件查询用户表信息
     *
     * @param user 条件
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.USER_ARRAY, key = "#root.args")
    public JsonRequest<List<UserVo>> lookUser(Integer current, Integer size, User user) {
        IPage<UserVo> userPage = baseMapper.selectUserPages(new Page<>(current, size), user);
        return JsonRequest.success(userPage.getRecords(), userPage.getTotal());
    }

    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return JSON
     */
    @Override
    @ExcludeMethods
    public JsonRequest<String> login(String phone, String password) {
        // 根据手机号查询用户表信息
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
                .eq(User::getStatus, UserStatusEnum.NORMAL.getValue()));
        if (Objects.isNull(user)) {
            return JsonRequest.error(RequestException.LOGIN_ERROR);
        }
        // 判断密码是否正确
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            return JsonRequest.error(RequestException.LOGIN_ERROR);
        }
        // 生成token
        String token = TokenUtils.createToken(user.getId());
        // 异步编排
        // 缓存用户信息
        redisClientsUtils.set(
                CacheKeysConstants.UserKeys.USER_TOKEN + user.getId(),
                token,
                CacheKeysConstants.CacheTimes.TOKEN_TIME);
        // 将当前用户信息放入redis缓存
        redisClientsUtils.setGlobalData(UserConstants.ID, user.getId());
        // 查询当前用户已经拥有的资源
        JsonRequest<List<Router>> jsonRequest = routerService.lookRouterByUserId(user.getId());
        // 将拥有的资源放入redis缓存
        redisClientsUtils.set(CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY + token,
                jsonRequest.getData(),
                CacheKeysConstants.CacheTimes.PERMISSION_LIST);
        return JsonRequest.success(token);
    }

    /**
     * 根据token查询当前用户信息
     *
     * @param token token
     * @return JSON
     */
    @Override
    @Cacheable(value = CacheKeysConstants.UserKeys.USER_LOGIN, key = "#token")
    public JsonRequest<UserVo> lookCurrentUser(String token) {
        if (!StringUtils.hasText(token)) {
            return JsonRequest.error(RequestException.TOKEN_ERROR);
        }
        Long id = TokenUtils.checkToken(token, Long.class);
        if (Objects.isNull(id)) {
            return JsonRequest.error(RequestException.TOKEN_ERROR);
        }
        UserVo userVo = baseMapper.selectUserVoById(id);
        // 判断用户表是否存在
        if (Objects.isNull(userVo)) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        // 将当前用户信息放入redis缓存
        redisClientsUtils.setGlobalData(UserConstants.ID, id);
        // 查询当前用户已经拥有的资源
        JsonRequest<List<Router>> jsonRequest = routerService.lookRouterByUserId(id);
        // 将拥有的资源放入redis缓存
        redisClientsUtils.set(CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY + token,
                jsonRequest.getData(),
                CacheKeysConstants.CacheTimes.PERMISSION_LIST);
        // 返回
        return JsonRequest.success(userVo);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return JSON
     */
    @Override
    @CacheEvict(value = CacheKeysConstants.UserKeys.USER_ARRAY, allEntries = true)
    public JsonRequest<Integer> register(User user) {
        // 判断手机号是否已注册
        User userObject = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, user.getPhone()));
        if (Objects.nonNull(userObject)) {
            return JsonRequest.error(RequestException.REGISTER_ERROR);
        }
        // 用户名为空,默认设置手机号,防止空指针
        if (!StringUtils.hasText(user.getUsername())) {
            user.setUsername(user.getPhone());
        }
        // QQ为空,默认设置手机号
        if (!StringUtils.hasText(user.getQq())) {
            user.setQq(user.getPhone());
        }
        // 微信为空,默认设置手机号
        if (!StringUtils.hasText(user.getWechat())) {
            user.setWechat(user.getPhone());
        }
        //简化以上if语句
        // new DefaultValueSetter<User>()
        //         .addField("username", User::setUsername, User::getUsername)
        //         .addField("qq", User::setQq, User::getQq)
        //         .addField("wechat", User::0.setWechat, User::getWechat)
        //         .setDefaultValuesIfEmpty(user, user.getPhone());
        // 密码为空,默认设置随机密码
        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(IdUtil.fastSimpleUUID());
        }
        // 加密密码
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 插入数据
        int column = baseMapper.insert(user);
        if (column < 1) {
            throw new ServiceException(RequestException.INSERT_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 修改一条用户表信息
     *
     * @param user 用户表信息
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.USER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN}, allEntries = true)
    public JsonRequest<Integer> updateOneUser(User user) {
        // 密码置空
        user.setPassword(null);
        // 手机号置空
        user.setPhone(null);
        // 判断用户是否存在
        User userObject = baseMapper.selectById(user.getId());
        if (Objects.isNull(userObject)) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        // 判断邮箱,QQ,微信是否已占用
        long count = count(Wrappers.lambdaQuery(User.class)
                .ne(User::getId, user.getId())
                .and(wrapper -> wrapper
                        .eq(User::getEmail, user.getEmail())
                        .or()
                        .eq(User::getQq, user.getQq())
                        .or()
                        .eq(User::getWechat, user.getWechat())));
        if (count > 0) {
            return JsonRequest.error(RequestException.DATA_EXISTS_ERROR);
        }
        // 拿取token
        String token = redisClientsUtils.get(CacheKeysConstants.UserKeys.USER_TOKEN + user.getId());
        // 删除当前用户信息
        redisClientsUtils.remove(CacheKeysConstants.UserKeys.USER_LOGIN + "::" + token);
        int column = baseMapper.updateById(user);
        if (column < 1) {
            throw new ServiceException(RequestException.UPDATE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 批量删除用户表信息
     *
     * @param ids 用户表ID列表
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.USER_ARRAY, CacheKeysConstants.UserKeys.USER_ROUTER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN}, allEntries = true)
    public JsonRequest<Integer> deleteUsers(List<Long> ids) {
        // 查询用户表信息
        List<User> userList = baseMapper.selectBatchIds(ids);
        // 判断待删除的用户表信息是否存在
        if (userList.size() != ids.size()) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        int column = baseMapper.deleteByIds(ids);
        if (column < 1) {
            throw new ServiceException(RequestException.DELETE_ERROR);
        }
        return JsonRequest.success(column);
    }

    /**
     * 用户退出登录
     *
     * @param token token
     * @return JSON
     */
    @Override
    @ExcludeMethods
    public JsonRequest<Boolean> logout(String token) {
        // 解析token并获取用户ID
        Long userId = TokenUtils.checkToken(token, Long.class);
        // 删除token缓存
        Boolean remove1 = redisClientsUtils.remove(CacheKeysConstants.UserKeys.USER_TOKEN + userId);
        // 删除当前用户信息
        Boolean remove = redisClientsUtils.remove(CacheKeysConstants.UserKeys.USER_LOGIN + "::" + token);
        return JsonRequest.success(remove1 && remove);
    }

    /**
     * 获取手机验证码
     *
     * @param phone 手机号
     * @return JSON
     */
    @Override
    @ExcludeMethods
    public JsonRequest<Boolean> getCode(String phone) {
        // 生成8位验证码
        String code = IdUtil.fastSimpleUUID().substring(0, 7);
        // todo 这里以后替换成短信验证码
        log.info("验证码:{}", code);
        // 验证码15min有效
        redisClientsUtils.set(CacheKeysConstants.UserKeys.PHONE_CODE_ARRAY + phone, code, CacheKeysConstants.CacheTimes.PHONE_CODE_TIME);
        return JsonRequest.success(true);
    }

    /**
     * 校验手机验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return JSON
     */
    @Override
    @ExcludeMethods
    public JsonRequest<Boolean> checkCode(String phone, String code) {
        String code1 = redisClientsUtils.get(CacheKeysConstants.UserKeys.PHONE_CODE_ARRAY + phone);
        // 校验验证码
        if (!code1.equals(code)) {
            return JsonRequest.error(RequestException.PHONE_CODE_ERROR);
        }
        return JsonRequest.success(true);
    }

    /**
     * 修改密码
     *
     * @param updatePasswordDto 信息
     * @return JSON
     */
    @Override
    public JsonRequest<Boolean> updatePassword(UserUpdatePasswordDto updatePasswordDto) {
        User user = getOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getPhone, updatePasswordDto.getPhone())
                .eq(User::getStatus, UserStatusEnum.NORMAL.getValue()));
        if (Objects.isNull(user)) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        // 校验旧密码
        if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            return JsonRequest.error(RequestException.DATA_NOT_MATCH);
        }
        // 校验验证码
        JsonRequest<Boolean> checkCode = checkCode(updatePasswordDto.getPhone(), updatePasswordDto.getCode());
        if (!checkCode.getSuccess()) {
            return JsonRequest.error(RequestException.PHONE_CODE_ERROR);
        }
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        boolean column = updateById(newUser);
        if (!column) {
            throw new ServiceException(RequestException.UPDATE_ERROR);
        }
        // 拿取token
        String token = redisClientsUtils.get(CacheKeysConstants.UserKeys.USER_TOKEN + user.getId());
        // 删除token缓存
        Boolean remove1 = redisClientsUtils.remove(CacheKeysConstants.UserKeys.USER_TOKEN + user.getId());
        // 删除当前用户信息
        Boolean remove = redisClientsUtils.remove(CacheKeysConstants.UserKeys.USER_LOGIN + "::" + token);
        return JsonRequest.success(remove1 && remove);
    }

    /**
     * 修改手机号
     *
     * @param phoneDto 手机号信息
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.USER_ARRAY}, allEntries = true)
    public JsonRequest<Boolean> updatePhone(UserUpdatePhoneDto phoneDto) {
        ///////////////////////////////旧手机号///////////////////////////////
        // 根据手机号能否查询出用户
        User user = redisClientsUtils.get(CacheKeysConstants.UserKeys.USER_ARRAY + "::",
                phoneDto.getOldPhone(),
                (v) -> getOne(Wrappers.lambdaQuery(User.class)
                        .eq(User::getPhone, v)),
                User.class);
        if (Objects.isNull(user)) {
            return JsonRequest.error(RequestException.NOT_FOUND);
        }
        // 校验验证码
        JsonRequest<Boolean> checkCode = checkCode(phoneDto.getOldPhone(), phoneDto.getCode());
        if (!checkCode.getSuccess()) {
            return JsonRequest.error(RequestException.PHONE_CODE_ERROR);
        }
        ///////////////////////////////新手机号///////////////////////////////
        User oldUser = redisClientsUtils.get(CacheKeysConstants.UserKeys.USER_ARRAY + "::",
                phoneDto.getNewPhone(),
                (v) -> getOne(Wrappers.lambdaQuery(User.class)
                        .eq(User::getPhone, v)),
                User.class);
        if (Objects.nonNull(oldUser)) {
            return JsonRequest.error(RequestException.PHONE_EXISTS_ERROR);
        }
        // 校验验证码
        JsonRequest<Boolean> checkCode1 = checkCode(phoneDto.getNewPhone(), phoneDto.getNewCode());
        if (!checkCode1.getSuccess()) {
            return JsonRequest.error(RequestException.PHONE_CODE_ERROR);
        }
        ///////////////////////////////修改逻辑///////////////////////////////
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setPhone(phoneDto.getNewPhone());
        boolean column = updateById(newUser);
        if (!column) {
            throw new ServiceException(RequestException.UPDATE_ERROR);
        }
        // 拿取token
        String token = redisClientsUtils.get(CacheKeysConstants.UserKeys.USER_TOKEN + user.getId());
        // 删除token缓存
        Boolean remove1 = redisClientsUtils.remove(CacheKeysConstants.UserKeys.USER_TOKEN + user.getId());
        // 删除当前用户信息
        Boolean remove = redisClientsUtils.remove(CacheKeysConstants.UserKeys.USER_LOGIN + "::" + token);
        return JsonRequest.success(remove1 && remove);
    }

    /**
     * 导出excel
     *
     * @param response 响应
     * @return JSON
     */
    @Override
    public JsonRequest<Void> exportExcel(HttpServletResponse response) {
        ExcelUtils.exportExcel(
                () -> baseMapper.selectUserList(null),
                UserVo.class, response);
        return JsonRequest.success();
    }

    /**
     * 上传头像
     *
     * @param id       用户ID
     * @param avatarId 头像ID
     * @return JSON
     */
    @Override
    @CacheEvict(value = {CacheKeysConstants.UserKeys.USER_ARRAY, CacheKeysConstants.UserKeys.USER_LOGIN}, allEntries = true)
    public JsonRequest<Integer> uploadAvatar(Long id, Long avatarId) {
        // User user1 = getById(id);
        // if (Objects.isNull(user1) || !user1.getStatus().equals(UserStatusEnum.NORMAL.getValue())) {
        //     return JsonRequest.error(RequestException.NOT_FOUND);
        // }
        // JsonRequest<Boolean> image = fileFeign.isImage(avatarId);
        // if (!image.getData()) {
        //     throw new ServiceException(RequestException.FILE_ERROR);
        // }
        // User user = new User();
        // user.setId(id);
        // user.setAvatar(avatarId);
        // return JsonRequest.success(baseMapper.updateById(user));
        return null;
    }

}