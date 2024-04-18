create table oa_project_child
(
    id            bigint unsigned auto_increment comment '项目id'
        primary key,
    project_id    bigint unsigned                        null comment '主要项目id',
    name          varchar(100)                           not null comment '项目名称',
    principal_id  bigint unsigned                        not null comment '项目负责人',
    description   json                                   null comment '项目描述（技术选择，描述）',
    cycle         int unsigned                           not null comment '项目周期',
    work_load     int unsigned default '1'               not null comment '工作量（人天）',
    files         json                                   null comment '子项目文件',
    complete_time date                                   null comment '完成时间',
    created_at    timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at    timestamp                              null comment '更新时间',
    is_delete     tinyint(1)   default 0                 not null comment '项目是否删除',
    constraint oa_project_child_oa_user_id_fk
        foreign key (principal_id) references oa_user (id)
            on update cascade
)
    comment '项目表';