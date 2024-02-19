--liquibase formatted sql

--changeset nlyashkevich:1
CREATE TABLE click_links (
    id          BIGSERIAL   PRIMARY KEY,
    link_id     BIGINT      NOT NULL REFERENCES link(id),
    usage_time  TIMESTAMP   NOT NULL
);


