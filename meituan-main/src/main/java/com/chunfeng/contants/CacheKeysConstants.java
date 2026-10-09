package com.chunfeng.contants;

import java.time.Duration;

/**
 * 缓存字段
 *
 * @author by 春风能解释
 * <p>
 * 2024/8/4
 */
public interface CacheKeysConstants {
    /**
     * 权限模块
     */
    interface UserKeys {
        /**
         * 用户token缓存键
         */
        String USER_TOKEN = "user:user:token:";
        /**
         * 当前用户信息
         */
        String USER_LOGIN = "user:user:info";
        /**
         * 用户列表
         */
        String USER_ARRAY = "user:user:list";
        /**
         * 角色列表
         */
        String ROLE_ARRAY = "user:role:list";
        /**
         * 权限列表
         */
        String PERMISSION_ARRAY = "user:permission:list";
        /**
         * 路由列表
         */
        String ROUTER_ARRAY = "user:router:list";
        /**
         * 用户权限列表
         */
        String USER_PERMISSION_ARRAY = "user:user:permission:list";
        /**
         * 角色权限列表
         */
        String ROLE_PERMISSION_ARRAY = "user:role:permission:list";
        /**
         * 权限路由列表
         */
        String PERMISSION_ROUTER_ARRAY = "user:permission:router:list";
        /**
         * 用户路由列表
         */
        String USER_ROUTER_ARRAY = "user:user:router:list:";
        /**
         * 验证码
         */
        String CODE_ARRAY = "user:code:";
        /**
         * 手机验证码
         */
        String PHONE_CODE_ARRAY = "user:code:phone:";
    }

    /**
     * 考试模块
     */
    interface ExamKeys {
        /**
         * 题目列表
         */
        String PROBLEM_ARRAY = "exam:problem:list";
        /**
         * 做题历史
         */
        String PROBLEM_USER_ARRAY = "exam:problem:user:list";
        /**
         * 题库列表
         */
        String EXAM_ARRAY = "exam:problem_bank:list";
        /**
         * 考试记录列表
         */
        String EXAM_USER_ARRAY = "exam:problem_bank:user:list";
        /**
         * 题库绑定数据
         */
        String EXAM_USER_LIST = "exam:problem_bank:problem:list:";
        /**
         * 题库总数统计
         */
        String EXAM_COUNT = "exam:statistics:count";
        /**
         * 题库类型统计
         */
        String EXAM_TYPE = "exam:statistics:pie_type";
    }

    /**
     * 招聘模块
     */
    interface RecruitKeys {
        /**
         * 岗位列表
         */
        String POST_ARRAY = "recruit:post:list";
        /**
         * 岗位分类树
         */
        String POST_CATEGORY_TREE = "recruit:post_category:tree";
        /**
         * 岗位分类列表
         */
        String POST_CATEGORY_LIST = "recruit:post_category:list";
        /**
         * 简历列表
         */
        String RESUME_ARRAY = "recruit:resume:list";
        /**
         * 投递列表
         */
        String POST_RESUME_ARRAY = "recruit:post:resume:list";
        /**
         * 已投递岗位列表
         */
        String RESUME_POST_ARRAY = "recruit:resume:post:list";
        /**
         * 工作经历列表
         */
        String WORK_EXPERIENCE_ARRAY = "recruit:resume:work:list";
        /**
         * 项目经历列表
         */
        String PROJECT_EXPERIENCE_ARRAY = "recruit:resume:project:list";
        /**
         * 学历经历列表
         */
        String EDUCATION_EXPERIENCE_ARRAY = "recruit:resume:education:list";
        /**
         * 招聘统计折线
         */
        String RECRUIT_STATISTICS_LINE = "recruit:statistics:post:line";
        /**
         * 招聘统计总数
         */
        String RECRUIT_STATISTICS_COUNT = "recruit:statistics:count";
        /**
         * 招聘统计柱状
         */
        String RECRUIT_STATISTICS_PIE = "recruit:statistics:post:pie";
    }

    /**
     * 课程模块
     */
    interface CourseKeys {
        /**
         * 课程列表
         */
        String COURSE_ARRAY = "course:course:list";
        /**
         * 科目列表
         */
        String SUBJECT_ARRAY = "course:subject:list";
        /**
         * 科目节点列表
         */
        String COURSE_NODE_ARRAY = "course:course_node:list";
        /**
         * 课程用户列表
         */
        String COURSE_USER_ARRAY = "course:course_user:list";
        /**
         * 课程总数统计
         */
        String COURSE_COUNT = "course:statistics:count";
        /**
         * 课程类型统计
         */
        String COURSE_TYPE = "course:statistics:pie_type";
    }

    /**
     * 文件模块
     */
    interface FileKeys {
        /**
         * 文件列表
         */
        String FILE_ARRAY = "file:file:list";
        /**
         * 是否为图片
         */
        String FILE_TYPE = "file:file:is_image";
        /**
         * 文件块
         */
        String FILE_CHUNK = "file:file:chunk:";
    }

    /**
     * 消息模块
     */
    interface MessageKeys {
        /**
         * 消息列表
         */
        String MESSAGE_LIST = "message:message:list";
    }

    /**
     * 文档模块
     */
    interface DocumentKeys {
        /**
         * 文档列表
         */
        String DOCUMENT_LIST = "document:document:list";
        /**
         * 文档审核临时列表
         */
        String DOCUMENT_AUDIT = "document:document_audit:list";
        /**
         * 文档标签列表
         */
        String DOCUMENT_TAG_LIST = "document:document_tag:list";
        /**
         * 评论列表
         */
        String DOCUMENT_COMMENT = "document:comment:list";
    }

    /**
     * 公共Key
     */
    interface CommonKeys {
        /**
         * 分布式共享变量
         */
        String GLOBAL_DATA = "global:map:";
        /**
         * 分布式锁
         */
        String GLOBAL_LOCK = "global:lock:";
    }

    /**
     * 缓存时间
     */

    interface CacheTimes {
        /**
         * 随机时间范围[-x,+x]
         */
        Integer RANDOM_RANGE = 120;
        /**
         * token过期时间
         */
        Duration TOKEN_TIME = Duration.ofMinutes(15);
        /**
         * 手机验证码过期时间
         */
        Duration PHONE_CODE_TIME = Duration.ofMinutes(15);
        /**
         * 权限列表过期时间
         */
        Duration PERMISSION_LIST = Duration.ofMinutes(30);
        /**
         * 验证码过期时间
         */
        Duration CODE_TIME = Duration.ofMinutes(5);
    }
}
