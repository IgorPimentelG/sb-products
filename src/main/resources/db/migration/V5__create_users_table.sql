CREATE TABLE IF NOT EXISTS users (
  id CHAR(36) PRIMARY KEY NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(30) NOT NULL,
  account_non_expired BIT(1) DEFAULT 1,
  account_non_locked BIT(1) DEFAULT 1,
  credentails_non_expired BIT(1) DEFAULT 1,
  enabled BIT(1) DEFAULT 1,

  CONSTRAINT ck_password CHECK (LENGTH(password) >= 8)
);