package com.chunfeng.aop;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.result.exception.ServiceException;
import com.chunfeng.result.exenum.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;

/**
 * 全局异常处理器
 * <p>
 * 此类用于处理大部分异常信息的响应,经过了这个类的处理,返回的格式将统一变为
 * <pre>{@code
 * {
 *  "status": 200,
 *  "message": "请求已成功!",
 *  "success": true,
 *  "type": "SUCCESS",
 *  "data": "",
 *  "date": 1697117527763
 * }}</pre>
 * 此响应类可参照{@link JsonRequest}的字段
 *
 * @author by 春风能解释
 * <p>
 * 2023/8/4
 */
@Component
@Slf4j
public class ExceptionHandler extends DefaultErrorAttributes {

    /**
     * 异常信息
     */
    private ServiceException exception = new ServiceException(RequestException.UNKNOWN_EXCEPTION);

    /**
     * 自定义错误返回
     *
     * @param webRequest 当前请求
     * @param options    错误上下文
     * @return JSON
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        // 初始化异常
        super.getErrorAttributes(webRequest, options);
        // 获取对应的JSON字符串
        JsonRequest<Void> request = JsonRequest.error(exception);
        // 先转换为JSON
        String jsonString = request.toString();
        log.warn("全局异常处理类已捕获到异常:{}", request.getMessage());
        log.warn("详情异常信息如下:{}", jsonString);
        // 再转换为Map对象
        return JSON.parseObject(jsonString, new TypeReference<>() {
        });
    }

    /**
     * 获取异常信息
     *
     * @param webRequest 当前请求
     * @return 异常
     */
    @Override
    public Throwable getError(WebRequest webRequest) {
        // 获取异常信息
        Throwable error = super.getError(webRequest);
        // 将其强转为业务层定义的异常
        if (error instanceof ServiceException) {
            this.exception = (ServiceException) error;
        }
        // 异常为空,则为空指针异常
        if (Objects.isNull(error)) {
            exception = new ServiceException(RequestException.NOT_FOUND);
            return exception;
        }
        // 传递消息
        exception.setMessage(error.getLocalizedMessage());
        return error;
    }

}
