CREATE TABLE Customer (
    customerId INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INT,
    gender ENUM('Male', 'Female', 'Other') DEFAULT 'Other',
    email_address VARCHAR(255) UNIQUE,
    pan_number VARCHAR(10) UNIQUE,
    aadhar_card_number VARCHAR(12) UNIQUE,
    customer_unique_id VARCHAR(100) UNIQUE
);
