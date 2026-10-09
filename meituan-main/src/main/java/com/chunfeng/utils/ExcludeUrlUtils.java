package com.chunfeng.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.chunfeng.entity.Router;
import com.chunfeng.properties.ExcludeUrlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 排除路径判断
 *
 * @author by 春风能解释
 * <p>
 * 2023/10/12
 */
@Slf4j
public class ExcludeUrlUtils {

    /**
     * 排除路径
     */
    private static final ExcludeUrlProperties excludeUrlProperties = SpringUtil.getBean(ExcludeUrlProperties.class);

    /**
     * 判断路由是否在用户拥有的路由之中
     *
     * @param router  路由
     * @param routers 路由集合
     * @return 是否匹配
     */
    public static boolean isMatch(Router router, List<Router> routers) {
        // 判断路由是否为空
        if (Objects.isNull(router) || routers.isEmpty()) {
            return false;
        }
        // 通配符匹配
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return routers.stream()
                .anyMatch(userRouter ->
                        router.getMethod().equals(userRouter.getMethod()) &&
                                pathMatcher.match(userRouter.getUri(), router.getUri()));
    }

    /**
     * 判断原路径是否在已定义的排除路径之中
     *
     * @param origin 源路径
     * @return 静态与匿名路径表示法如下:
     * <ul>
     *     <li>0为不包含于任何</li>
     *     <li>1为包含于匿名路径</li>
     *     <li>2为包含于静态路径</li>
     * </ul>
     * 以上均可扩充
     */
    public static int isExcludeUrl(String origin) {
        // 通配符匹配
        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 匿名路径
        if (Arrays.stream(excludeUrlProperties.getExcludeUrl())
                .anyMatch(excludeV -> pathMatcher.match(excludeV, origin))) {
            log.info("路径{}为匿名路径", origin);
            return 1;
        }
        //静态路径
        if (Arrays.stream(excludeUrlProperties.getStaticUrl())
                .anyMatch(staticV -> pathMatcher.match(staticV, origin))) {
            log.info("路径{}为静态路径", origin);
            return 2;
        }
        return 0;
    }

    /**
     * 判断是否为xss路径
     *
     * @param origin 路径
     * @return 是否为xss路径
     */
    public static boolean isXssUrl(String origin) {
        // 通配符匹配
        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 匿名路径
        if (Arrays.stream(excludeUrlProperties.getXssUrl())
                .anyMatch(xssV -> pathMatcher.match(xssV, origin))) {
            log.info("路径{}为xss排除路径", origin);
            return true;
        }
        return false;
    }
}
