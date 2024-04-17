create table oa_project_modules
(
    id               bigint unsigned auto_increment comment '模块id'
        primary key,
    project_child_id bigint unsigned                        not null comment '子项目id',
    name             varchar(100)                           not null comment '模块名称',
    principal_id     bigint unsigned                        not null comment '模块负责人',
    description      json                                   null comment '项目描述（技术选择，描述）',
    work_load        int unsigned default '1'               not null comment '工作量（人天）',
    complete_time    datetime                               null comment '完成时间',
    created_at       timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at       timestamp                              null comment '更新时间',
    is_delete        tinyint(1)   default 0                 not null comment '项目是否删除',
    dead_line        timestamp                              not null comment '子模块的截止时间',
    status        varchar(8)   default 'progress'        not null comment '模块状态（draft: 草稿，progress: 进行，pause: 暂停，abnormal: 异常，complete: 完成）',
    constraint oa_project_modules_oa_user_id_fk
        foreign key (principal_id) references oa_user (id)
            on update cascade
)
    comment '模块表';

