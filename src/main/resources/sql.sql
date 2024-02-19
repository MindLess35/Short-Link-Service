select * from link_statistics;
select * from users;
select * from link;
select * from databasechangelog;
select * from databasechangeloglock;

SELECT username, email -- COUNT(*) = 1
FROM users
WHERE username = '1' OR email = '3';

explain analyze
SELECT COUNT(username) = 1
FROM users
WHERE username = '1d'
UNION ALL
SELECT COUNT(email) = 1
FROM users
WHERE email = '1';

ALTER TABLE link
DROP key;

ALTER TABLE link
ADD key VARCHAR(255);

drop table if exists link_statistics cascade;
drop table if exists link cascade;
drop table if exists users cascade;
drop table databasechangelog;
drop table databasechangeloglock;

truncate link restart identity cascade;
truncate users restart identity cascade;
truncate link_statistics restart identity cascade;

analyze link;
explain analyze
select original_link from link where short_link_name = 'f';

insert into users(username, email, role) values
('1','1','USER');

drop table databasechangelog;
drop table databasechangeloglock;
drop table if exists users_aud;
drop table if exists users_audit;
drop table if exists revision;
drop table link;

select * from link_statistics;
select * from users;
select * from token;
select * from link;
select * from databasechangelog;
select * from databasechangeloglock;
select * from users_audit;
select * from revision;
select * from users_aud;

alter table revision
alter column created_by
drop not null;


delete
from token
where user_id = 5 OR user_id = 6;

delete
from users
where password = '1';

truncate link restart identity cascade;
truncate token restart identity cascade;
truncate users restart identity cascade;
truncate revision restart identity cascade;
truncate users_audit restart identity cascade;
truncate link_statistics restart identity cascade;
truncate link restart identity cascade;

ALTER TABLE link
DROP key;

ALTER TABLE link
ADD key VARCHAR(255);


CREATE TABLE users (
	id          BIGSERIAL       PRIMARY KEY,
	username    VARCHAR(255)    NOT NULL UNIQUE,
	email       VARCHAR(255)    NOT NULL UNIQUE,
	password    VARCHAR(255)    NOT NULL,
	role        VARCHAR(32)     NOT NULL
);


CREATE TABLE link (
	id                  BIGSERIAL       PRIMARY KEY,
	original_link       VARCHAR(2048)   NOT NULL,
	short_link_name     VARCHAR(128)    NOT NULL UNIQUE,
	user_id             BIGINT          REFERENCES users(id)
);


CREATE TABLE link_statistics (
	id                  BIGSERIAL   PRIMARY KEY,
	link_id             BIGINT      NOT NULL REFERENCES link(id),
	date_of_creation    TIMESTAMP   NOT NULL,
	date_of_last_uses   TIMESTAMP   NOT NULL,
	life_time           BIGINT      NOT NULL,
	count_of_uses       BIGINT      NOT NULL
);

create table revision (
        id bigserial not null,
        created_at timestamp(6) not null,
        created_by varchar(255) not null,
        modified_at timestamp(6),
        modified_by varchar(255),
        timestamp bigint,
        primary key (id)
    )
Hibernate:
    create table users_aud (
        id bigint not null,
        rev bigint not null,
        revtype smallint,
        email varchar(255),
        password varchar(255),
        role varchar(255) check (role in ('USER','ADMIN','MANAGER')),
        username varchar(255),
        primary key (rev, id)
    )
Hibernate:
    alter table if exists users_aud
       add constraint FKmrjb3nxent1mi8jjld588s7u6
       foreign key (rev)
       references revision


---------------------------------------------------------------------------

Hibernate:
    create table revision (
        id bigserial not null,
        created_at timestamp(6),
        primary key (id)
    )
Hibernate:
    alter table if exists users
       add column created_at timestamp(6)
Hibernate:
    create table users_aud (
        id bigint not null,
        rev bigint not null,
        revtype smallint,
        created_at timestamp(6),
        created_by bigint,
        email varchar(255),
        modified_at timestamp(6),
        modified_by bigint,
        password varchar(255),
        role varchar(255) check (role in ('USER','ADMIN','MANAGER')),
        username varchar(255),
        primary key (rev, id)
    )
Hibernate:
    alter table if exists users_aud
       add constraint FKmrjb3nxent1mi8jjld588s7u6
       foreign key (rev)
       references revision


--------------------------------------------------------------------------- 33333333333333333



Hibernate:
    create table revision (
        id bigserial not null,
        created_at timestamp(6),
        primary key (id)
    )
Hibernate:
    create table users_aud (
        id bigint not null,
        rev bigint not null,
        revtype smallint,
        created_at timestamp(6),
        created_by bigint,
        email varchar(255),
        modified_at timestamp(6),
        modified_by bigint,
        password varchar(255),
        role varchar(255) check (role in ('USER','ADMIN','MANAGER')),
        username varchar(255),
        primary key (rev, id)
    )
Hibernate:
    alter table if exists users_aud
       add constraint FKmrjb3nxent1mi8jjld588s7u6
       foreign key (rev)
       references revision
