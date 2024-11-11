CREATE TABLE Manager (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INT,
    email_address VARCHAR(255) UNIQUE NOT NULL
);
