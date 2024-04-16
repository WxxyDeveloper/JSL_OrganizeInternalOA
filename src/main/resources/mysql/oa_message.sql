create table oa_message
(
    id         bigint unsigned auto_increment comment '消息主键'
        primary key,
    uid        bigint unsigned                      not null comment '用户主键',
    title      varchar(100)                         not null comment '消息抬头',
    text       text                                 not null comment '消息正文',
    is_delete  tinyint(1) default 0                 not null comment '消息是否删除',
    created_at timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    deleted_at timestamp                            null comment '删除时间',
    sid        bigint unsigned                      null comment '发送用户id',
    type       varchar(100)                         null comment '跳转类型',
    to_id      int unsigned                         null comment '跳转的id',
    constraint oa_message_oa_user_id_fk
        foreign key (uid) references oa_user (id)
            on update cascade on delete cascade
)
    comment '消息';

