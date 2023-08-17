CREATE TABLE IF NOT EXISTS user_permissions (
    id_user CHAR(32) NOT NULL,
    id_permission INT NOT NULL,

    PRIMARY KEY (id_user, id_permission),

    CONSTRAINT fk_user_permission FOREIGN KEY (id_user) REFERENCES users (id),
    CONSTRAINT fk_user_permission_permission FOREIGN KEY (id_permission) REFERENCES permissions (id)
);