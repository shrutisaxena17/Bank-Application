CREATE TABLE Address (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL,
    customer_unique_id VARCHAR(100),
    manager_id INT,
    CONSTRAINT fk_customer_address FOREIGN KEY (customer_unique_id) REFERENCES Customer(customer_unique_id) ON DELETE SET NULL,
    CONSTRAINT fk_manager_address FOREIGN KEY (manager_id) REFERENCES Manager(id) ON DELETE SET NULL
);
