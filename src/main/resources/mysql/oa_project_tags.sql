create table oa_project_tags
(
    id         bigint unsigned auto_increment comment '主键id'
        primary key,
    name       varchar(20)                          not null comment '标签名称',
    pid        bigint unsigned                      null comment '父标签id',
    created_at timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at timestamp                            null comment '修改时间',
    is_delete  tinyint(1) default 0                 not null comment '是否删除',
    constraint oa_project_tags_name_uindex
        unique (name),
    constraint oa_project_tags_oa_project_tags_id_fk
        foreign key (pid) references oa_project_tags (id)
            on update cascade on delete cascade
)
    comment '项目标签表';

