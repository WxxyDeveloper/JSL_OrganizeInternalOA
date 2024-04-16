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

create index oa_review_project_project_submodule_id_oa_project_work_id_fk
    on oa_review (project_module_id);

create index oa_review_project_subsystem_id_oa_project_work_id_fk
    on oa_review (project_child_id);

