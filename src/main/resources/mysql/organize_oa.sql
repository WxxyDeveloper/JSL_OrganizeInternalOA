SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `organize_oa`
--

-- --------------------------------------------------------

--
-- 表的结构 `oa_config`
--

CREATE TABLE `oa_config`
(
    `id`         bigint UNSIGNED NOT NULL COMMENT '主键',
    `value`      varchar(50)     NOT NULL COMMENT '调用关键字',
    `data`       json                     DEFAULT NULL COMMENT 'json数据',
    `created_at` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp       NULL     DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='配置数据表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_permissions`
--

CREATE TABLE `oa_permissions`
(
    `id`         bigint UNSIGNED NOT NULL COMMENT '主键',
    `pid`        bigint UNSIGNED          DEFAULT NULL COMMENT '权限父id',
    `name`       varchar(100)    NOT NULL COMMENT '权限名称',
    `code`       varchar(50)     NOT NULL COMMENT '权限编码',
    `type`       tinyint(1)      NOT NULL DEFAULT '1' COMMENT '0为菜单，1为权限',
    `deleted_at` timestamp       NULL     DEFAULT NULL COMMENT '删除时间(没有删除应当为空)'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='权限表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_project`
--

CREATE TABLE `oa_project`
(
    `id`               bigint UNSIGNED  NOT NULL COMMENT '项目id',
    `name`             varchar(255)     NOT NULL COMMENT '项目名称',
    `description`      varchar(255)     NOT NULL COMMENT '一句话描述',
    `introduction`     text             NOT NULL COMMENT '项目详细介绍',
    `code_open`        tinyint(1)       NOT NULL DEFAULT '0' COMMENT '代码是否开放',
    `core_code`        text COMMENT '核心代码内容（Markdown）',
    `git`              json                      DEFAULT NULL COMMENT 'git代码仓库内容',
    `difficulty_level` tinyint UNSIGNED NOT NULL DEFAULT '1' COMMENT '难度等级',
    `type`             int UNSIGNED     NOT NULL COMMENT '类型',
    `reward`           bigint UNSIGNED           DEFAULT NULL COMMENT '报酬',
    `status`           tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='项目内容表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_project_cutting`
--

CREATE TABLE `oa_project_cutting`
(
    `id`             bigint UNSIGNED  NOT NULL COMMENT '主键',
    `pid`            bigint UNSIGNED  NOT NULL COMMENT '项目id',
    `name`           varchar(40)      NOT NULL COMMENT '项目分割模块名字',
    `tag`            json                      DEFAULT NULL COMMENT '模块标签',
    `engineering`    tinyint UNSIGNED NOT NULL DEFAULT '1' COMMENT '工程量计算',
    `estimated_time` int UNSIGNED     NOT NULL DEFAULT '3' COMMENT '预估时间(小时)',
    `real_time`      timestamp        NULL     DEFAULT NULL COMMENT '实际时间',
    `created_at`     timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     timestamp        NULL     DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='项目切割';

-- --------------------------------------------------------

--
-- 表的结构 `oa_project_type`
--

CREATE TABLE `oa_project_type`
(
    `id`         int UNSIGNED NOT NULL COMMENT '项目类型id',
    `name`       varchar(50)  NOT NULL COMMENT '类型名字',
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp    NULL     DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='项目类型';

-- --------------------------------------------------------

--
-- 表的结构 `oa_project_user`
--

CREATE TABLE `oa_project_user`
(
    `id`         bigint UNSIGNED NOT NULL COMMENT '主键id',
    `uid`        bigint UNSIGNED NOT NULL COMMENT '用户id',
    `pid`        bigint UNSIGNED NOT NULL COMMENT '接到分割项目内容',
    `created_at` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp       NULL     DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户项目表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_role`
--

CREATE TABLE `oa_role`
(
    `id`         int UNSIGNED NOT NULL COMMENT '角色id',
    `role_name`  varchar(20)  NOT NULL COMMENT '角色名称',
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp    NULL     DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_role_permissions`
--

CREATE TABLE `oa_role_permissions`
(
    `rid`        int UNSIGNED    NOT NULL COMMENT '角色id',
    `pid`        bigint UNSIGNED NOT NULL COMMENT '权限id',
    `created_at` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色权限表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_role_user`
--

CREATE TABLE `oa_role_user`
(
    `uid`         bigint UNSIGNED NOT NULL COMMENT '用户id',
    `rid`         int UNSIGNED    NOT NULL COMMENT '角色id',
    `createdt_at` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  timestamp       NULL     DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色用户表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_user`
--

CREATE TABLE `oa_user`
(
    `id`                     bigint UNSIGNED  NOT NULL COMMENT '主键',
    `job_id`                 char(10)         NOT NULL COMMENT '工作ID：正则表达 "^[STU|TEA|OTH][0-9]{7}"',
    `username`               varchar(40)      NOT NULL COMMENT '用户名',
    `password`               varchar(255)     NOT NULL COMMENT '密码',
    `address`                varchar(255)     NOT NULL COMMENT '用户家庭地址',
    `phone`                  varchar(11)      NOT NULL COMMENT '电话',
    `email`                  varchar(100)     NOT NULL COMMENT '邮箱',
    `age`                    tinyint UNSIGNED NOT NULL COMMENT '年龄',
    `signature`              varchar(50)               DEFAULT NULL COMMENT '一句话描述自己',
    `sex`                    tinyint UNSIGNED NOT NULL DEFAULT '0' COMMENT '0/1/2:保密/男/女',
    `avatar`                 text COMMENT '头像地址',
    `nickname`               varchar(20)               DEFAULT NULL COMMENT '昵称',
    `enabled`                tinyint(1)       NOT NULL DEFAULT '1' COMMENT '账户是否可用',
    `account_no_expired`     tinyint(1)       NOT NULL DEFAULT '1' COMMENT '账户是否过期',
    `credentials_no_expired` tinyint(1)       NOT NULL DEFAULT '0' COMMENT '密码是否过期',
    `recommend`              tinyint(1)       NOT NULL DEFAULT '0' COMMENT '账户是否被推荐',
    `account_no_locked`      tinyint(1)       NOT NULL DEFAULT '1' COMMENT '账户是否被锁定',
    `description`            text COMMENT '个人简介',
    `created_at`             timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`             timestamp        NULL     DEFAULT NULL COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户表';

-- --------------------------------------------------------

--
-- 表的结构 `oa_review`
--


create table oa_review
(
    id                bigint unsigned auto_increment comment '主键'
        primary key,
    name              varchar(255)                       not null comment '申请名称',
    content           longtext                           not null comment '申请理由',
    sender_id         bigint unsigned                    not null comment '申请者用户id',
    recipient_id      bigint unsigned                    null comment '审核者用户id',
    category          tinyint  default 1                 null comment '审核类别（0：子系统；1：子模块）',
    project_id        bigint unsigned                    not null comment '申请的项目id',
    project_child_id  bigint unsigned                    not null comment '申请的子系统id',
    project_module_id bigint unsigned                    null comment '申请的子模块id',
    application_time  datetime default CURRENT_TIMESTAMP not null comment '申请时间',
    review_time       datetime                           null comment '审核时间',
    review_result     tinyint  default 2                 not null comment '审核结果（0：未通过；1：通过；2：未审批）',
    is_delete         tinyint  default 0                 not null comment '是否删除（0：未删除；1：已删除）',
    created_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_time      datetime                           null comment '更新时间',
    constraint oa_review_recipient_id_oa_user_id_fk
        foreign key (recipient_id) references oa_user (id)
            on update cascade on delete cascade,
    constraint oa_review_sender_id_oa_user_id_fk
        foreign key (sender_id) references oa_user (id)
            on update cascade on delete cascade
);

create index oa_review_project_id_oa_project_id_fk
    on oa_review (project_id);



--
-- 转储表的索引
--

--
-- 表的索引 `oa_config`
--
ALTER TABLE `oa_config`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `oa_config_value_uindex` (`value`);

--
-- 表的索引 `oa_permissions`
--
ALTER TABLE `oa_permissions`
    ADD PRIMARY KEY (`id`),
    ADD KEY `oa_permissions_oa_permissions_id_fk` (`pid`);

--
-- 表的索引 `oa_project`
--
ALTER TABLE `oa_project`
    ADD PRIMARY KEY (`id`),
    ADD KEY `oa_project_oa_project_type_id_fk` (`type`);

--
-- 表的索引 `oa_project_cutting`
--
ALTER TABLE `oa_project_cutting`
    ADD PRIMARY KEY (`id`),
    ADD KEY `oa_project_cutting_oa_project_id_fk` (`pid`);

--
-- 表的索引 `oa_project_type`
--
ALTER TABLE `oa_project_type`
    ADD PRIMARY KEY (`id`);

--
-- 表的索引 `oa_project_user`
--
ALTER TABLE `oa_project_user`
    ADD PRIMARY KEY (`id`),
    ADD KEY `oa_project_user_oa_project_cutting_id_fk` (`pid`),
    ADD KEY `oa_user_project_oa_user_id_fk` (`uid`);

--
-- 表的索引 `oa_role`
--
ALTER TABLE `oa_role`
    ADD PRIMARY KEY (`id`);

--
-- 表的索引 `oa_role_permissions`
--
ALTER TABLE `oa_role_permissions`
    ADD PRIMARY KEY (`rid`),
    ADD KEY `oa_role_permissions_oa_permissions_id_fk` (`pid`);

--
-- 表的索引 `oa_role_user`
--
ALTER TABLE `oa_role_user`
    ADD PRIMARY KEY (`uid`),
    ADD KEY `oa_role_user_oa_role_id_fk` (`rid`);

--
-- 表的索引 `oa_user`
--
ALTER TABLE `oa_user`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `oa_user_job_id_uindex` (`job_id`),
    ADD UNIQUE KEY `oa_user_email_uindex` (`email`),
    ADD UNIQUE KEY `oa_user_phone_uindex` (`phone`),
    ADD UNIQUE KEY `oa_user_username_uindex` (`username`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `oa_config`
--
ALTER TABLE `oa_config`
    MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';

--
-- 使用表AUTO_INCREMENT `oa_permissions`
--
ALTER TABLE `oa_permissions`
    MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';

--
-- 使用表AUTO_INCREMENT `oa_project`
--
ALTER TABLE `oa_project`
    MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '项目id';

--
-- 使用表AUTO_INCREMENT `oa_project_cutting`
--
ALTER TABLE `oa_project_cutting`
    MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';

--
-- 使用表AUTO_INCREMENT `oa_project_type`
--
ALTER TABLE `oa_project_type`
    MODIFY `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '项目类型id';

--
-- 使用表AUTO_INCREMENT `oa_project_user`
--
ALTER TABLE `oa_project_user`
    MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id';

--
-- 使用表AUTO_INCREMENT `oa_role`
--
ALTER TABLE `oa_role`
    MODIFY `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id';

--
-- 使用表AUTO_INCREMENT `oa_user`
--
ALTER TABLE `oa_user`
    MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键';

--
-- 限制导出的表
--

--
-- 限制表 `oa_permissions`
--
ALTER TABLE `oa_permissions`
    ADD CONSTRAINT `oa_permissions_oa_permissions_id_fk` FOREIGN KEY (`pid`) REFERENCES `oa_permissions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `oa_project`
--
ALTER TABLE `oa_project`
    ADD CONSTRAINT `oa_project_oa_project_type_id_fk` FOREIGN KEY (`type`) REFERENCES `oa_project_type` (`id`) ON UPDATE CASCADE;

--
-- 限制表 `oa_project_cutting`
--
ALTER TABLE `oa_project_cutting`
    ADD CONSTRAINT `oa_project_cutting_oa_project_id_fk` FOREIGN KEY (`pid`) REFERENCES `oa_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `oa_project_user`
--
ALTER TABLE `oa_project_user`
    ADD CONSTRAINT `oa_project_user_oa_project_cutting_id_fk` FOREIGN KEY (`pid`) REFERENCES `oa_project_cutting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `oa_user_project_oa_user_id_fk` FOREIGN KEY (`uid`) REFERENCES `oa_user` (`id`);

--
-- 限制表 `oa_role_permissions`
--
ALTER TABLE `oa_role_permissions`
    ADD CONSTRAINT `oa_role_permissions_oa_permissions_id_fk` FOREIGN KEY (`pid`) REFERENCES `oa_permissions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `oa_role_permissions_oa_role_id_fk` FOREIGN KEY (`rid`) REFERENCES `oa_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `oa_role_user`
--
ALTER TABLE `oa_role_user`
    ADD CONSTRAINT `oa_role_user_oa_role_id_fk` FOREIGN KEY (`rid`) REFERENCES `oa_role` (`id`),
    ADD CONSTRAINT `oa_role_user_oa_user_id_fk` FOREIGN KEY (`uid`) REFERENCES `oa_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
