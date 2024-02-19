--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE link_statistics
ADD CONSTRAINT link_stat_unique_link_id UNIQUE(link_id);

--changeset nlyashkevich:2
ALTER TABLE link_statistics
ALTER COLUMN link_id SET NOT NULL;



