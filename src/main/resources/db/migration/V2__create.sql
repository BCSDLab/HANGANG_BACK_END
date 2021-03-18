/*
create table if not exists hangang.user (

	id ㅋ`` unsigned auto_increment primary key,
    portal_account varchar(50) unique not null,
    password text not null,
    nickname varchar(50) not null,
    salt varchar(255) default 0,
    profile_image_url varchar(255),
    point int(10) unsigned default 20,
    is_deleted tinyint(1) default 0,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
)default character set utf8 collate utf8_general_ci;

create table if not exists hangang.major (
	id bigint unsigned auto_increment primary key,
    user_id bigint unsigned not null,
    major varchar(20) not null,
    is_deleted tinyint default 0
)default character set utf8 collate utf8_general_ci;

create table if not exists hangang.auth_number(
	id bigint unsigned auto_increment primary key,
    ip varchar(40) not null,
    portal_account varchar(50) not null,
    secret varchar(50) not null,
    flag int not null,
    is_authed tinyint default 0,
    is_deleted tinyint default 0,
    expired_at timestamp not null,
    created_at timestamp default current_timestamp
)default character set utf8 collate utf8_general_ci;

create table if not exists  hangang.user_point_history(
    id BIGINT PRIMARY KEY auto_increment,
    user_id BIGINT NOT NULL,
    variance BIGINT NOT NULL DEFAULT 0,
    point_type_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted tinyint default 0
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if not exists  hangang.point_type(
    id BIGINT PRIMARY KEY auto_increment,
    title VARCHAR(30) NOT NULL
)default character set utf8 collate utf8_general_ci;

 */
/*** 강의 테이블
CREATE TABLE IF NOT EXISTS hangang.lecture (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `semester_date` varchar(6) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `department` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `professor` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `professor_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `classification` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `is_deleted` tinyint(1) DEFAULT '0',
    `last_reviewed_at` timestamp DEFAULT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
 ***/

/*** 종합 평가 테이블
CREATE TABLE IF NOT EXISTS hangang.total_evaluation(
     `id` bigint unsigned NOT NULL AUTO_INCREMENT primary key,
     `lecture_id` bigint unsigned NOT NULL,
     `rating` float,
     `assignment_amount` int,
     `difficulty` float,
     `grade_portion` float,
     `attendance_frequency` int,
     `test_times` int,
     `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;
***/

