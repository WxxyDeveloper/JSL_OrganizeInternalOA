create table oa_role_permissions
(
    rid        int unsigned                        not null comment 'Role ID',
    pid        bigint unsigned                     not null comment 'Permission ID',
    created_at timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    primary key (rid, pid)
);

create index oa_role_permission_oa_permissions_id_fk
    on oa_role_permissions (pid);