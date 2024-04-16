create table oa_user
(
    id                     bigint unsigned auto_increment comment '主键'
        primary key,
    job_id                 char(10)                                   not null comment '工作ID：正则表达 "^[STU|TEA|OTH][0-9]{7}"',
    username               varchar(40)                                not null comment '用户名',
    password               varchar(255)                               not null comment '密码',
    address                varchar(255)                               not null comment '用户家庭地址',
    phone                  varchar(11)                                not null comment '电话',
    email                  varchar(100)                               not null comment '邮箱',
    age                    tinyint unsigned                           not null comment '年龄',
    signature              varchar(50)                                null comment '一句话描述自己',
    sex                    tinyint unsigned default '0'               not null comment '0/1/2:保密/男/女',
    avatar                 text                                       null comment '头像地址',
    nickname               varchar(20)                                null comment '昵称',
    enabled                tinyint(1)       default 1                 not null comment '账户是否可用',
    account_no_expired     tinyint(1)       default 1                 not null comment '账户是否过期',
    credentials_no_expired tinyint(1)       default 0                 not null comment '密码是否过期',
    recommend              tinyint(1)       default 0                 not null comment '账户是否被推荐',
    account_no_locked      tinyint(1)       default 1                 not null comment '账户是否被锁定',
    description            text                                       null comment '个人简介',
    created_at             timestamp        default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at             timestamp                                  null comment '更新时间',
    is_delete              tinyint(1)       default 0                 not null,
    constraint oa_user_email_uindex
        unique (email),
    constraint oa_user_job_id_uindex
        unique (job_id),
    constraint oa_user_phone_uindex
        unique (phone),
    constraint oa_user_username_uindex
        unique (username)
)
    comment '用户表';