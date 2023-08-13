CREATE TABLE IF NOT EXISTS products(
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    price DECIMAL NOT NULL,

    CONSTRAINT CK_price CHECK (price > 0)
);