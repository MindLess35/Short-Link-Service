--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE users
ADD profile_image VARCHAR(255);
--rollback
--ALTER TABLE users
--DROP COLUMN profile_image;
