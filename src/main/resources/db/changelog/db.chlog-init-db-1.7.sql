--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE link
RENAME COLUMN short_link_name TO short_link;



