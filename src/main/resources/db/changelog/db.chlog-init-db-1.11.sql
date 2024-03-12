--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE link_statistics
DROP CONSTRAINT IF EXISTS link_statistics_link_id_fkey;

--changeset nlyashkevich:2
ALTER TABLE link_statistics
ADD CONSTRAINT link_statistics_link_id_fkey
FOREIGN KEY (link_id) REFERENCES link (id) ON DELETE SET NULL;
