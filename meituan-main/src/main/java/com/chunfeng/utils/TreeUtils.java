package com.chunfeng.utils;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.Supplier2;
import com.chunfeng.result.exception.ServiceException;

import java.util.List;

/**
 * 树工具类
 *
 * @author by 春风能解释
 * <p>
 * 2025/4/16
 */
public class TreeUtils {


    /**
     * 构建树形结构
     *
     * @param data
     * @param parentId
     * @param getId
     * @param getParentId
     * @param setChildren
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> List<T> buildTree(List<T> data, E parentId, Func1<T, E> getId, Func1<T, E> getParentId, Supplier2<T, T, List<T>> setChildren) {
        return data.stream()
                .filter(item -> {
                    try {
                        return getParentId.call(item).equals(parentId);
                    } catch (Exception e) {
                        throw new ServiceException(e);
                    }
                })
                .map(children -> {
                    try {
                        return setChildren.get(children, buildTree(data, getId.call(children), getId, getParentId, setChildren));
                    } catch (Exception e) {
                        throw new ServiceException(e);
                    }
                })
                .toList();
    }
}
