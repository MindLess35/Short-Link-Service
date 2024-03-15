--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link
RENAME COLUMN short_link_name TO short_link;
--rollback
--ALTER TABLE link
--RENAME COLUMN short_link TO short_link_name;