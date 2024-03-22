--liquibase formatted sql

--changeset Nikita Lyashkevich:1
CREATE TABLE users_audit (
    revtype         SMALLINT,
    id              BIGINT          NOT NULL,
    revision_id     BIGINT          NOT NULL,
    email           VARCHAR(255),
    password        VARCHAR(255),
    role            VARCHAR(255),
    username        VARCHAR(255),
    PRIMARY KEY (id, revision_id)
);
--rollback DROP TABLE users_audit;

