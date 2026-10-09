package com.chunfeng.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 条件构造器
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/26
 */
@NoArgsConstructor
public class QueryUtils {

    /**
     * 构造查询条件(自定义对象)
     *
     * @param <T>    类型
     * @param query  查询条件
     * @param clazz  查询类型
     * @param ignore 排除字段
     * @return 查询条件
     */
    @SneakyThrows
    public static <T> QueryChainWrapper<T> wrapperQueryBuilder(T query, Class<T> clazz, String... ignore) {
        if (Objects.isNull(query)) {
            return null;
        }
        Field[] fields = ReflectUtil.getFields(query.getClass());
        QueryChainWrapper<T> queryWrapper = new QueryChainWrapper<>(clazz);
        List<String> ignoreFields = Arrays.asList(ignore);
        // 获取成员变量列表
        for (Field field : fields) {
            // 排除字段
            if (ignore.length > 0 && ignoreFields.contains(field.getName())) {
                continue;
            }
            // 获取字段值
            Object value = field.get(query);
            if (Objects.isNull(value)) {
                continue;
            }
            String columnName;
            TableField tableField = field.getAnnotation(TableField.class);
            // 字段上有注解(使用给定的列名)
            if (Objects.nonNull(tableField) && StringUtils.hasText(tableField.value())) {
                columnName = tableField.value();
            }
            // 没有注解(下划线)
            else {
                columnName = StrUtil.toUnderlineCase(field.getName());
            }
            // 字符串默认模糊查询
            if (value instanceof String) {
                queryWrapper.like(columnName, value);
            }
            // 其他类型直接精确查询
            else {
                queryWrapper.eq(columnName, value);
            }
        }
        return queryWrapper;
    }
}
