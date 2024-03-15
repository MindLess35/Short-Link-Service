--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link_statistics
DROP CONSTRAINT link_statistics_link_id_fkey;
--rollback
--ALTER TABLE link_statistics
--ADD CONSTRAINT link_statistics_link_id_fkey
--FOREIGN KEY (link_id)
--REFERENCES link (id);

--changeset Nikita Lyashkevich:2
ALTER TABLE link_statistics
ADD CONSTRAINT link_statistics_link_id_fkey
FOREIGN KEY (link_id) REFERENCES link (id) ON DELETE SET NULL;
--rollback
--ALTER TABLE link_statistics
--DROP CONSTRAINT link_statistics_link_id_fkey;