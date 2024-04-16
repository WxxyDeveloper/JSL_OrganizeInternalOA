create table oa_user_tags
(
    id         bigint unsigned auto_increment comment '标签主键'
        primary key,
    name       varchar(20)                          not null comment '标签名',
    pid        bigint unsigned                      null comment '标签父id',
    created_at timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at timestamp                            null comment '修改时间',
    is_delete  tinyint(1) default 0                 not null comment '是否删除',
    constraint oa_user_tags_name_uindex
        unique (name)
)
    comment '用户标签';