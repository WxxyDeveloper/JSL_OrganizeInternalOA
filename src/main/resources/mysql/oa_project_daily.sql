create table oa_project_daily
(
    id         bigint unsigned auto_increment comment '日报主键'
        primary key,
    user_id    bigint unsigned                      not null comment '用户id',
    project_id bigint unsigned                      not null comment '项目id',
    content    text                                 not null comment '日报内容',
    daily_time date                                 not null comment '日志发布时间',
    created_at timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at timestamp                            null comment '修改时间',
    is_delete  tinyint(1) default 0                 not null comment '是否删除',
    constraint oa_project_daily_oa_project_id_fk
        foreign key (project_id) references oa_project (id)
            on update cascade on delete cascade,
    constraint oa_project_daily_oa_user_id_fk
        foreign key (user_id) references oa_user (id)
            on update cascade
)
    comment '项目日报';