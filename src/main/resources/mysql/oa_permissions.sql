create table oa_permissions
(
    id         bigint unsigned auto_increment comment '主键'
        primary key,
    pid        bigint unsigned      null comment '权限父id',
    name       varchar(100)         not null comment '权限名称',
    code       varchar(50)          not null comment '权限编码',
    type       tinyint(1) default 1 not null comment '0为菜单，1为权限',
    deleted_at timestamp            null comment '删除时间(没有删除应当为空)',
    constraint oa_permissions_oa_permissions_id_fk
        foreign key (pid) references oa_permissions (id)
            on update cascade on delete cascade
)
    comment '权限表';