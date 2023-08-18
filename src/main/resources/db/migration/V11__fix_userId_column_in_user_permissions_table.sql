ALTER TABLE user_permissions DROP CONSTRAINT fk_user_permission;
ALTER TABLE user_permissions MODIFY COLUMN id_user VARCHAR(36) NOT NULL;
ALTER TABLE user_permissions ADD FOREIGN KEY (id_user) REFERENCES users (id);