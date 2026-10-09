package com.chunfeng.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.chunfeng.result.exception.ServiceException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Excel工具类
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/24
 */
public class ExcelUtils {

    /**
     * 导入Excel
     *
     * @param excel     excel表格
     * @param clazz     导入类型
     * @param insertFun 插入方法
     * @param <T>       导入类型
     * @param <R>       结果类型
     * @return 导入结果
     */
    @SneakyThrows
    public static <T, R> R importExcel(MultipartFile excel, Class<T> clazz, Function<List<T>, R> insertFun) {
        // 文件为空
        if (excel.isEmpty()) {
            return null;
        }
        ExcelReader reader = ExcelUtil.getReader(excel.getInputStream());
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            // 得到API注解
            Schema schema = field.getAnnotationsByType(Schema.class)[0];
            // 判断注解值是否有效
            if (Objects.nonNull(schema)) {
                reader.addHeaderAlias(StringUtils.hasText(schema.description()) ?
                                schema.description() : field.getName(),
                        field.getName());
            }
        }
        List<T> list = reader.readAll(clazz);
        // 判断数据是否为空
        if (list.isEmpty()) {
            return null;
        }
        return insertFun.apply(list);
    }


    /**
     * 导出Excel
     *
     * @param selectFun 查询方法
     * @param clazz     导出类型
     * @param response  响应体
     * @param <T>       导出类型
     */
    public static <T> void exportExcel(Supplier<List<T>> selectFun, Class<T> clazz, HttpServletResponse response) {
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<T> list = selectFun.get();
            if (list.isEmpty()) {
                return;
            }
            String fileName = clazz.getName();
            // 设置响应头
            Schema schemaClass = clazz.getAnnotation(Schema.class);
            if (Objects.nonNull(schemaClass)) {
                fileName = schemaClass.name();
            }
            response.setContentType(ExcelUtil.XLS_CONTENT_TYPE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename=" +
                            java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                            + ".xls");
            // 获取所有字段
            Field[] fields = ReflectUtil.getFields(clazz);
            for (Field field : fields) {
                Schema schemaField = field.getAnnotation(Schema.class);
                // 判断注解值是否有效
                if (Objects.nonNull(schemaField)) {
                    writer.addHeaderAlias(field.getName(), StringUtils.hasText(schemaField.description()) ?
                            schemaField.description() : field.getName()
                    );
                }
            }
            writer.write(list);
            writer.flush(response.getOutputStream(), true);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
