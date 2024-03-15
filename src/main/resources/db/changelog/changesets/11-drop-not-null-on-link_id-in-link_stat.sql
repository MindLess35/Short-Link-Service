--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link_statistics
ALTER COLUMN link_id
DROP NOT NULL;
--rollback
--ALTER TABLE link_statistics
--ALTER COLUMN link_id
--SET NOT NULL;
