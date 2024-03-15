--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE click_links
ALTER COLUMN link_id
DROP NOT NULL;
--rollback
--ALTER TABLE click_links
--ALTER COLUMN link_id
--SET NOT NULL;



