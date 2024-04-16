create table oa_config
(
    id         bigint unsigned auto_increment comment '主键'
        primary key,
    value      varchar(50)                         not null comment '调用关键字',
    data       json                                null comment 'json数据',
    created_at timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at timestamp                           null comment '修改时间',
    constraint oa_config_value_uindex
        unique (value)
)
    comment '配置数据表';