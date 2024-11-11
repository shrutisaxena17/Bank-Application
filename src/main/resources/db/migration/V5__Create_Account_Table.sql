CREATE TABLE Account (
    accountId INT AUTO_INCREMENT PRIMARY KEY,
    customer_unique_id VARCHAR(100) NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    accountBalance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    accountType ENUM('Savings', 'Checking', 'Business') NOT NULL,
    currency VARCHAR(10) DEFAULT 'RS',
    status ENUM('Active', 'Inactive') DEFAULT 'Active',
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_customer_account FOREIGN KEY (customer_unique_id) REFERENCES Customer(customer_unique_id) ON DELETE CASCADE
);
