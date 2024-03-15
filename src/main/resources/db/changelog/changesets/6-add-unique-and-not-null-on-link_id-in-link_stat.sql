--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link_statistics
ADD CONSTRAINT link_stat_unique_link_id UNIQUE(link_id);
--rollback
--ALTER TABLE link_statistics
--DROP CONSTRAINT link_stat_unique_link_id;

--changeset Nikita Lyashkevich:2
ALTER TABLE link_statistics
ALTER COLUMN link_id
SET NOT NULL;
--rollback
--ALTER TABLE link_statistics
--ALTER COLUMN link_id
--DROP NOT NULL;




