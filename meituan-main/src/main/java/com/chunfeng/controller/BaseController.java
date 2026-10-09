package com.chunfeng.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chunfeng.result.JsonRequest;
import com.chunfeng.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 基本的Controller
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/20
 */
@Getter
@NoArgsConstructor
public class BaseController<S extends IService<T>, T> {
    /**
     * 注入业务层
     */
    @Autowired
    protected S baseService;

    /**
     * 导出excel
     *
     * @param response 响应
     */
    @GetMapping("/export")
    @Operation(summary = "导出excel")
    public void export(
            @Parameter(description = "响应")
            HttpServletResponse response) {
        ExcelUtils.exportExcel(() -> baseService.list(), baseService.getEntityClass(), response);
    }


    /**
     * 导入excel
     *
     * @param file excel文件
     * @return JSON
     */
    @PostMapping("/import")
    @Operation(summary = "导入excel")
    public JsonRequest<Boolean> export(
            @Parameter(description = "excel文件")
            @RequestPart @RequestParam("excel") MultipartFile file) {
        return JsonRequest.success(ExcelUtils.importExcel(file, baseService.getEntityClass(), baseService::saveBatch));
    }
}
