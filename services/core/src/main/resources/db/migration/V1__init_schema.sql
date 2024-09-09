-- Users Table
CREATE TABLE users
(
    id    VARCHAR(255) PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Purchases Table
CREATE TABLE purchases
(
    id             VARCHAR(255) PRIMARY KEY,
    purchase_name  VARCHAR(255) NOT NULL,
    payment_method VARCHAR(50)  NOT NULL,
    user_id        VARCHAR(255),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Payments Table
CREATE TABLE payments
(
    id            VARCHAR(255) PRIMARY KEY,
    purchase_id   VARCHAR(255)   NOT NULL,
    purchase_name VARCHAR(255)   NOT NULL,
    amount        DECIMAL(19, 2) NOT NULL,
    payment       VARCHAR(50)    NOT NULL,
    CONSTRAINT fk_purchase FOREIGN KEY (purchase_id) REFERENCES purchases (id) ON DELETE CASCADE
);
