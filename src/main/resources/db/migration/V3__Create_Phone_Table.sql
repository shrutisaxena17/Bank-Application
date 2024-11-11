CREATE TABLE Phone (
    id INT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    type ENUM('Mobile', 'Home', 'Work'),
    customer_unique_id VARCHAR(100),
    manager_id INT,
    CONSTRAINT fk_customer FOREIGN KEY (customer_unique_id) REFERENCES Customer(customer_unique_id) ON DELETE SET NULL,
    CONSTRAINT fk_manager FOREIGN KEY (manager_id) REFERENCES Manager(id) ON DELETE SET NULL
);
