create table oa_permissions
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    name        varchar(100)                        not null comment '权限名称',
    description varchar(100)                        not null comment '权限描述',
    created_at  timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint oa_permissions_name_uindex
        unique (name)
)
    comment '权限表';