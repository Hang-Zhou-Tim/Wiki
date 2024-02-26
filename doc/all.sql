drop table if exists `test`;
create table `test`
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    `password` varchar(50) comment 'pass',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='test';

insert into `test` (id, name, password) values (1,'test','password');


drop table if exists `demo`;
create table demo
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='test';


insert into `demo` (id, name) values (1,'test');


drop table if exists `ebook`;
create table `ebook` (
                         `id` bigint not null comment 'id',
                         `name` varchar(50) comment 'name',
                         `category1_id` bigint comment 'category 1',
                         `category2_id` bigint comment 'category 2',
                         `description` varchar(200) comment 'description',
                         `cover` varchar(200) comment 'cover',
                         `doc_count` int not null default 0 comment 'doc count',
                         `view_count` int not null default 0 comment 'view count',
                         `vote_count` int not null default 0 comment 'vote count',
                         primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='Ebook';

insert into `ebook` (id, name, description) values (1, 'Spring Boot Beginner Module', 'Java Development for Beginner，First-Choice Framework in Industrial-Level Application');
insert into `ebook` (id, name, description) values (2, 'Vue Module Beginner Module', 'Vue Development for Beginner，First-Choice Framework in Industrial-Level Application');
insert into `ebook` (id, name, description) values (3, 'Python Beginner Module', 'Python Development for Beginner，First-Choice Framework in Industrial-Level Application');
insert into `ebook` (id, name, description) values (4, 'Mysql Beginner Module', 'MySql Development for Beginner，First-Choice Framework in Industrial-Level Application');
insert into `ebook` (id, name, description) values (5, 'Oracle Beginner Module', 'Oracle Development for Beginner，First-Choice Framework in Industrial-Level Application');


drop table if exists `category`;
create table `category` (
                            `id` bigint not null comment 'id',
                            `parent` bigint not null default 0 comment 'parent id',
                            `name` varchar(50) not null comment 'name',
                            `sort` int comment 'sort',
                            primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='Category';

insert into `category` (id, parent, name, sort) values (100, 000, 'Front-End Development', 100);
insert into `category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `category` (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, 'Basic App', 201);
insert into `category` (id, parent, name, sort) values (202, 200, 'Framework APP', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort) values (301, 300, 'Basic App', 301);
insert into `category` (id, parent, name, sort) values (302, 300, 'Advanced App', 302);
insert into `category` (id, parent, name, sort) values (400, 000, 'Database', 400);
insert into `category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `category` (id, parent, name, sort) values (500, 000, 'Others', 500);
insert into `category` (id, parent, name, sort) values (501, 500, 'Server', 501);
insert into `category` (id, parent, name, sort) values (502, 500, 'Dev Tool', 502);
insert into `category` (id, parent, name, sort) values (503, 500, 'Top Language', 503);

-- 文档表
drop table if exists `doc`;
create table `doc` (
                       `id` bigint not null comment 'id',
                       `ebook_id` bigint not null default 0 comment 'Ebook id',
                       `parent` bigint not null default 0 comment 'Parent id',
                       `name` varchar(50) not null comment 'Name',
                       `sort` int comment 'Sequence',
                       `view_count` int default 0 comment 'View Count',
                       `vote_count` int default 0 comment 'Vote Count',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='Doc Table';

insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, 'Sample 1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, 'Sample 1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, 'Sample 2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, 'Sample 2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, 'Sample 2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, 'Sample 2.2.1', 1, 0, 0);

-- Content... It is so-called vertical sharding.
drop table if exists `content`;
create table `content` (
                           `id` bigint not null comment 'Document Id',
                           `content` mediumtext not null comment 'Content',
                           primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='Document Content';