--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE click_links
ALTER COLUMN link_id
DROP NOT NULL;



