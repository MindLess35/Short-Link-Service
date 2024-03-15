--liquibase formatted sql

--changeset Nikita Lyashkevich:1
CREATE TABLE click_links (
    id          BIGSERIAL   PRIMARY KEY,
    link_id     BIGINT      NOT NULL REFERENCES link(id),
    usage_time  TIMESTAMP   NOT NULL
);
--rollback DROP TABLE click_links;



