--liquibase formatted sql

--changeset Nikita Lyashkevich:1
CREATE TABLE users (
	id          BIGSERIAL       PRIMARY KEY,
	username    VARCHAR(255)    NOT NULL UNIQUE,
	email       VARCHAR(255)    NOT NULL UNIQUE,
	password    VARCHAR(255)    NOT NULL,
	role        VARCHAR(16)     NOT NULL
);
--rollback DROP TABLE users;

--changeset Nikita Lyashkevich:2
CREATE TABLE link (
	id                  BIGSERIAL       PRIMARY KEY,
	original_link       VARCHAR(1024)   NOT NULL,
	short_link_name     VARCHAR(128)    NOT NULL UNIQUE,
	user_id             BIGINT          REFERENCES users(id)
);
--rollback DROP TABLE link;

--changeset Nikita Lyashkevich:3
CREATE TABLE link_statistics (
	id                  BIGSERIAL   PRIMARY KEY,
	link_id             BIGINT      NOT NULL REFERENCES link(id),
	date_of_creation    TIMESTAMP   NOT NULL,
	date_of_last_uses   TIMESTAMP   NOT NULL,
	life_time           TIMESTAMP,
	count_of_uses       BIGINT      NOT NULL
);
--rollback DROP TABLE link_statistics;

