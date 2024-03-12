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
drop table databasechangelog;
drop table databasechangeloglock;
drop table if exists users_aud;
drop table if exists users_audit;
drop table if exists revision;
drop table link;


select * from click_links;
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

truncate click_links restart identity cascade;
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

select count(*) from link_statistics where id = 100;

SELECT TO_CHAR(date_of_creation, 'dd-mm-yyyy:hh')
FROM link_statistics;



select
    l1_0.id,
    l1_0.original_link,
    l1_0.short_link,
    to_char(ls1_0.date_of_creation, 'yyyy-MM-dd HH:mm:ss'),
    coalesce(to_char(ls1_0.date_of_last_uses, 'YYYY-MM-DD HH24:MI:SS'), 'Not used yet!')  as date,
    ls1_0.count_of_uses
from
    link l1_0
        join
    link_statistics ls1_0
    on l1_0.id = ls1_0.link_id
where
    l1_0.id > -1 and true
order by
    ls1_0.date_of_creation desc nulls last
offset
    1 rows
    fetch
    first 10 rows only;




select to_char(cl1_0.usage_time, 'dd-mm-yyyy : HH24'), count(*)
from click_links cl1_0
where cl1_0.link_id = 9 and to_char(cl1_0.usage_time, 'dd-mm-yyyy') = '27-02-2024'
group by to_char(cl1_0.usage_time, 'dd-mm-yyyy : HH24')
order by to_char(cl1_0.usage_time, 'dd-mm-yyyy : HH24');


SELECT TO_CHAR(cl.usage_time, :timeUnits), COUNT(*)
FROM click_links cl
WHERE cl.link_id = :linkId AND TO_CHAR(cl.usage_time, 'dd-mm-yyyy') = :onDate
GROUP BY TO_CHAR(cl.usage_time, :timeUnits)
ORDER BY TO_CHAR(cl.usage_time, :timeUnits)


select to_char(cl1_0.usage_time, 'dd-mm-yyyy : HH24-mi') as tu, count(*)
from click_links cl1_0
where cl1_0.link_id = 9 and to_char(cl1_0.usage_time, 'dd-mm-yyyy') = '27-02-2024'
group by tu
order by tu;

select to_char(cl1_0.usage_time,?),count(*)
from click_links cl1_0
where cl1_0.link_id=? and to_char(cl1_0.usage_time,'dd-mm-yyyy')=?
group by to_char(cl1_0.usage_time,?)
order by to_char(cl1_0.usage_time,?)


SELECT *
FROM link;

SELECT regexp_replace(original_link, '^(https?://[^/?#]+).*$', '\1'), original_link, id
FROM link;

ALTER TABLE click_links
    ALTER COLUMN link_id
        DROP NOT NULL;

drop table click_links;

CREATE TABLE click_links (
                             id          BIGSERIAL   PRIMARY KEY,
                             link_id     BIGINT      REFERENCES link(id) ON DELETE SET NULL,
                             usage_time  TIMESTAMP   NOT NULL
);

SELECT pg_reload_conf(); -- обновление конфигурации
SELECT pg_stat_reset(); -- сброс статистики запросов

select * from click_links;
select * from link_statistics;
select * from users;
select * from token;
select * from link;

delete
from
    link
where
    id= 4000;


ALTER TABLE link_statistics
    DROP CONSTRAINT link_statistics_link_id_key;



ALTER TABLE link_statistics
    DROP CONSTRAINT link_stat_unique_link_id;


ALTER TABLE link_statistics
    ALTER COLUMN link_id
        DROP NOT NULL;

ALTER TABLE link_statistics
    DROP CONSTRAINT fkqy0niivxyilm4elk3cd8w8pjw;

ALTER TABLE link_statistics
    ADD CONSTRAINT link_statistics_link_id_fkey FOREIGN KEY(link_id) REFERENCES link(id);

ALTER TABLE link_statistics
    ADD CONSTRAINT link_statistics_link_id_fkey
        FOREIGN KEY(link_id) REFERENCES link(id) ON DELETE SET NULL;
ALTER TABLE link_statistics
    drop CONSTRAINT link_statistics_link_id_fkey;










