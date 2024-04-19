create table oa_project
(
    id            bigint unsigned auto_increment comment '项目id'
        primary key,
    name          varchar(255)                           not null unique comment '项目名称',
    principal_id  bigint unsigned                        not null comment '项目负责人',
    description   json                                   null comment '项目描述（技术选择，描述）',
    tags          json                                   null comment '项目标签（项目类型：web，大数据等）',
    cycle         int unsigned                           not null comment '项目周期',
    work_load     int unsigned default '1'               not null comment '工作量（人天）',
    files         json                                   null comment '项目文件',
    begin_time    datetime     default CURRENT_TIMESTAMP not null comment '项目开始时间',
    complete_time date                                   null comment '完成时间',
    dead_line     date                                   not null comment '甲方要求结束',
    status        varchar(8)   default 'progress'        not null comment '项目状态（draft: 草稿，progress: 进行，pause: 暂停，abnormal: 异常，complete: 完成）',
    created_at    timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at    timestamp                              null comment '修改时间',
    is_delete     tinyint(1)   default 0                 not null comment '项目是否删除',
    constraint oa_project_oa_user_id_fk
        foreign key (principal_id) references oa_user (id)
            on update cascade
)
    comment '项目表';

