create table oa_news_user
(
    id         int unsigned auto_increment comment '主键'
        primary key,
    uid        bigint unsigned                     not null comment '用户id',
    nid        bigint unsigned                     not null comment '新闻id',
    created_at timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at timestamp                           null comment '修改时间',
    constraint oa_news_user_nid_oa_news_id_fk
        foreign key (nid) references oa_news (id)
            on update cascade,
    constraint oa_news_user_uid_oa_user_id_fk
        foreign key (uid) references oa_user (id)
            on update cascade
);