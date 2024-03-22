--liquibase formatted sql

--changeset Nikita Lyashkevich:1
CREATE TABLE revision (
    id          BIGSERIAL   PRIMARY KEY,
    timestamp   TIMESTAMP
);
--rollback DROP TABLE revision;


