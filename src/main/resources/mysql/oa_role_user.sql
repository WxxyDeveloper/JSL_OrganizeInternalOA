create table oa_role_user
(
    uid        bigint unsigned                     not null comment '用户id'
        primary key,
    rid        int unsigned                        not null comment '角色id',
    created_at timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at timestamp                           null comment '修改时间',
    constraint oa_role_user_oa_role_id_fk
        foreign key (rid) references oa_role (id),
    constraint oa_role_user_oa_user_id_fk
        foreign key (uid) references oa_user (id)
            on update cascade on delete cascade
)
    comment '角色用户表';