CREATE DATABASE `meituan`;

USE `meituan`;

CREATE TABLE `user`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键',
    `username`    VARCHAR(64)        NOT NULL COMMENT '用户名',
    `password`    VARCHAR(255)       NOT NULL COMMENT '密码',
    `age`         INT                         DEFAULT 18 NOT NULL COMMENT '年龄',
    `role_id`     BIGINT                      DEFAULT 1 NOT NULL COMMENT '角色ID',
    `sex`         INT                NOT NULL DEFAULT 2 COMMENT '性别(0女,1男,2保密)',
    `email`       VARCHAR(320) COMMENT '邮箱',
    `qq`          VARCHAR(16) COMMENT 'QQ号',
    `wechat`      VARCHAR(20) COMMENT '微信号',
    `address`     VARCHAR(255) COMMENT '地址',
    `avatar`      VARCHAR(1024) COMMENT '头像',
    `phone`       VARCHAR(11) UNIQUE NOT NULL COMMENT '手机号',
    `status`      INT                NOT NULL DEFAULT 0 COMMENT '状态(0正常,1冻结)',
    `deleted`     INT                NOT NULL DEFAULT 0 COMMENT '逻辑删除(0正常,1删除状态)',
    `create_time` VARCHAR(24)        NOT NULL COMMENT '创建时间',
    `create_user` BIGINT             NOT NULL COMMENT '创建人',
    `update_time` VARCHAR(24) COMMENT '更新时间',
    `update_user` BIGINT COMMENT '更新人'
) COMMENT '用户表';
CREATE TABLE `role`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键',
    `name`        VARCHAR(32) NOT NULL COMMENT '角色名',
    `deleted`     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除(0正常,1删除状态)',
    `create_time` VARCHAR(24) NOT NULL COMMENT '创建时间',
    `create_user` BIGINT      NOT NULL COMMENT '创建人',
    `update_time` VARCHAR(24) COMMENT '更新时间',
    `update_user` BIGINT COMMENT '更新人'
) COMMENT '角色表';
/*CREATE TABLE `permission`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键',
    `name`        VARCHAR(32) NOT NULL COMMENT '权限名',
    `level`       VARCHAR(64) NOT NULL COMMENT '系统级别(system,option,user)',
    `entity`      VARCHAR(64) NOT NULL COMMENT '作用实体',
    `option`      VARCHAR(64) NOT NULL COMMENT '操作(select,insert,update,delete)',
    `deleted`     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除(0正常,1删除状态)',
    `create_time` VARCHAR(24) NOT NULL COMMENT '创建时间',
    `create_user` BIGINT      NOT NULL COMMENT '创建人',
    `update_time` VARCHAR(24) COMMENT '更新时间',
    `update_user` BIGINT COMMENT '更新人'
) COMMENT '权限表';*/
CREATE TABLE `permission`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键',
    `name`        VARCHAR(32)  NOT NULL COMMENT '权限名',
    `path`        VARCHAR(64)  NOT NULL COMMENT '请求路径',
    `title`       VARCHAR(255) NOT NULL COMMENT '标题',
    `note`        VARCHAR(255) COMMENT '备注',
    `icon`        VARCHAR(64) COMMENT '图标',
    `component`   VARCHAR(64)  NOT NULL COMMENT '组件',
    `redirect`    VARCHAR(64) COMMENT '重定向路径',
    `affix`       INT          NOT NULL DEFAULT 0 COMMENT '是否固定(0否,1是)',
    `hidden`      INT          NOT NULL DEFAULT 0 COMMENT '是否隐藏(0否,1是)',
    `parent_id`   BIGINT       NOT NULL DEFAULT -1 COMMENT '父级ID',
    `deleted`     INT          NOT NULL DEFAULT 0 COMMENT '逻辑删除(0正常,1删除状态)',
    `create_time` VARCHAR(24)  NOT NULL COMMENT '创建时间',
    `create_user` BIGINT       NOT NULL COMMENT '创建人',
    `update_time` VARCHAR(24) COMMENT '更新时间',
    `update_user` BIGINT COMMENT '更新人'
) COMMENT '权限表';
CREATE TABLE `permission_role`
(
    `id`            BIGINT PRIMARY KEY COMMENT '主键',
    `role_id`       BIGINT      NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT      NOT NULL COMMENT '权限ID',
    `deleted`       INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除(0正常,1删除状态)',
    `create_time`   VARCHAR(24) NOT NULL COMMENT '创建时间',
    `create_user`   BIGINT      NOT NULL COMMENT '创建人',
    `update_time`   VARCHAR(24) COMMENT '更新时间',
    `update_user`   BIGINT COMMENT '更新人'
) COMMENT '角色-权限联表';
CREATE TABLE `router`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键',
    `name`        VARCHAR(32) NOT NULL COMMENT '路由名',
    `uri`         VARCHAR(32) NOT NULL COMMENT '请求路径',
    `method`      VARCHAR(8)  NOT NULL COMMENT '请求方式',
    `deleted`     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除(0正常,1删除状态)',
    `create_time` VARCHAR(24) NOT NULL COMMENT '创建时间',
    `create_user` BIGINT      NOT NULL COMMENT '创建人',
    `update_time` VARCHAR(24) COMMENT '更新时间',
    `update_user` BIGINT COMMENT '更新人'
) COMMENT '路由表';
CREATE TABLE `permission_router`
(
    `id`            BIGINT PRIMARY KEY COMMENT '主键',
    `permission_id` BIGINT      NOT NULL COMMENT '权限ID',
    `router_id`     BIGINT      NOT NULL COMMENT '路由ID',
    `deleted`       INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除(0正常,1删除状态)',
    `create_time`   VARCHAR(24) NOT NULL COMMENT '创建时间',
    `create_user`   BIGINT      NOT NULL COMMENT '创建人',
    `update_time`   VARCHAR(24) COMMENT '更新时间',
    `update_user`   BIGINT COMMENT '更新人'
) COMMENT '权限-路由联表';