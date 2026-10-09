package com.chunfeng.result.exenum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 请求异常的枚举
 * <p>
 * 用于枚举业务层所有的异常(可扩展)，一个枚举包含三个部分，状态码+消息+异常类型:
 * <br>
 * <ul>
 * <li>错误的唯一标识，可以是自定义的，也可以是WEB规定的，可参阅{@link HttpStatus}</li>
 * <li>消息用于向客户端或服务端描述问题的原因或具体情况，可根据实际业务需要进行设置</li>
 * <li>异常类型用于标识此异常可能发生在哪里，可参照{@link TypeEnum}</li>
 * </ul>
 *
 * @author by 春风能解释
 * <p>
 * 2023/6/30
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RequestException {
    /**
     * 非法授权
     */
    FORBIDDEN(401, "非法授权!", TypeEnum.CLIENT.getDescription()),
    /**
     * 登录失败
     */
    LOGIN_ERROR(402, "登录失败,请检查用户名或密码是否正确!", TypeEnum.CLIENT.getDescription()),
    /**
     * 非法访问
     */
    UNAUTHORIZED(403, "非法访问!", TypeEnum.CLIENT.getDescription()),
    /**
     * 找不到资源
     */
    NOT_FOUND(404, "找不到资源!", TypeEnum.CLIENT.getDescription()),
    /**
     * 注册失败
     */
    REGISTER_ERROR(405, "注册失败,请检查数据是否符合规范!", TypeEnum.CLIENT.getDescription()),
    /**
     * 文件超出范围
     */
    FILE_BEYOND_MAX_SIZE(406, "文件/分片大小超出范围!", TypeEnum.CLIENT.getDescription()),
    /**
     * token异常
     */
    TOKEN_ERROR(407, "非法token!", TypeEnum.CLIENT.getDescription()),
    /**
     * 退出登录失败
     */
    LOGOUT_ERROR(408, "退出登录失败!", TypeEnum.CLIENT.getDescription()),
    /**
     * 该数据已经存在
     */
    DATA_EXISTS_ERROR(409, "该数据已经存在!", TypeEnum.CLIENT.getDescription()),
    /**
     * 手机号已被占用
     */
    PHONE_EXISTS_ERROR(410, "手机号已被占用!", TypeEnum.CLIENT.getDescription()),
    /**
     * 验证码校验失败
     */
    PHONE_CODE_ERROR(411, "验证码错误", TypeEnum.CLIENT.getDescription()),
    /**
     * 文件类型不合法
     */
    FILE_TYPE_ERROR(412, "非法的文件类型!", TypeEnum.CLIENT.getDescription()),
    /**
     * 数据不匹配
     */
    DATA_NOT_MATCH(413, "传递的某个数据与数据库不匹配!", TypeEnum.CLIENT.getDescription()),
    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION(500, "未知错误,请联系网站管理员!", TypeEnum.UNKNOWN.getDescription()),
    /**
     * 查询数据时遇到问题
     */
    SELECT_ERROR(501, "数据查询失败!", TypeEnum.SERVER.getDescription()),
    /**
     * 添加数据时遇到问题
     */
    INSERT_ERROR(502, "数据添加失败!", TypeEnum.SERVER.getDescription()),
    /**
     * 修改数据时遇到问题
     */
    UPDATE_ERROR(503, "数据修改失败!", TypeEnum.SERVER.getDescription()),
    /**
     * 删除数据时遇到问题
     */
    DELETE_ERROR(504, "数据删除失败!", TypeEnum.SERVER.getDescription()),
    /**
     * 文件上传失败
     */
    FILE_ERROR(505, "文件上传失败!", TypeEnum.SERVER.getDescription()),
    /**
     * 空指针或消息为空
     */
    NULL_POINTER_ERROR(506, "空指针或消息为空!", TypeEnum.SERVER.getDescription()),
    /**
     * 验证码生成异常
     */
    CAPTCHA_GENERATED_ERROR(507, "验证码生成异常!", TypeEnum.SERVER.getDescription()),
    /**
     * 验证码校验异常
     */
    CAPTCHA_VERIFY_ERROR(508, "验证码校验异常!", TypeEnum.SERVER.getDescription()),
    /**
     * 文件导出失败
     */
    FILE_EXPORT_ERROR(509, "文件导出失败!", TypeEnum.SERVER.getDescription()),
    /**
     * 文件导入失败
     */
    FILE_IMPORT_ERROR(510, "文件导入失败!", TypeEnum.SERVER.getDescription()),
    /**
     * 该数据存在绑定关系
     */
    DATA_EXISTS_BINDING(511, "该数据存在绑定关系,操作失败!", TypeEnum.SERVER.getDescription());
    /**
     * 错误代码
     */
    private Integer status;
    /**
     * 消息
     */
    private String message;
    /**
     * 错误类型
     */
    private String type;
}
