CREATE TABLE IF NOT EXISTS permissions (
  id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  role VARCHAR(255) NOT NULL UNIQUE
);