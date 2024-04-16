create table oa_role
(
    id           int unsigned auto_increment comment '角色id'
        primary key,
    role_name    varchar(20)                         not null comment '角色名称',
    display_name varchar(10)                         null comment '中文描述',
    created_at   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at   timestamp                           null comment '修改时间'
)
    comment '角色表';