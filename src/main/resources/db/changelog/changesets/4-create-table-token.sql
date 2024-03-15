--liquibase formatted sql

--changeset Nikita Lyashkevich:1
CREATE TABLE token (
    id                  BIGSERIAL       PRIMARY KEY,
    token               VARCHAR(1024)   NOT NULL UNIQUE,
    token_type          VARCHAR(16)     NOT NULL,
    revoked             BOOLEAN         NOT NULL,
    expired             BOOLEAN         NOT NULL,
    date_of_creation    TIMESTAMP       NOT NULL,
    user_id             BIGINT          NOT NULL REFERENCES users(id)
);
--rollback DROP TABLE token;
