--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE users_audit
ADD profile_image VARCHAR(255);
--rollback
--ALTER TABLE users_audit
--DROP COLUMN profile_image;
