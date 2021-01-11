create table if not exists hangang.user (
	id bigint unsigned auto_increment primary key,
    portal_account varchar(50) unique not null,
    password text not null,
    nickname varchar(50) not null unique,
    salt varchar(255) default 0,
    profile_image_url varchar(255),
    point int(10) unsigned default 60,
    is_deleted tinyint(1) default 0,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
)default character set utf8 collate utf8_general_ci;

create table if not exists hangang.major (
	id bigint unsigned auto_increment primary key,
    user_id bigint unsigned not null,
    major varchar(20) not null
)default character set utf8 collate utf8_general_ci;

create table if not exists hangang.auth_number(
	id bigint unsigned auto_increment primary key,
    ip varchar(40) not null,
    portal_account varchar(50) not null,
    secret varchar(50) not null,
    flag int not null,
    is_authed tinyint default 0,
    expired_at timestamp not null,
    created_at timestamp default current_timestamp
)default character set utf8 collate utf8_general_ci;