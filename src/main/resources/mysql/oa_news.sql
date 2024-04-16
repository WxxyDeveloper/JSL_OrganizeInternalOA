create table oa_news
(
    id         bigint unsigned auto_increment comment '主键'
        primary key,
    title      varchar(255)                        not null comment '标题',
    content    text                                not null comment '内容',
    tags       varchar(10)                         null comment '标签（项目，通知）',
    likes      int       default 0                 not null comment '点赞数',
    comments   int       default 0                 not null comment '评论数',
    status     tinyint                             not null comment '状态（0：草稿；1：发布；2：隐藏）',
    created_at timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at timestamp                           null comment '更新时间'
);