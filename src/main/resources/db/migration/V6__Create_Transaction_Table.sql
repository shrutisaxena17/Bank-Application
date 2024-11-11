CREATE TABLE Transaction (
    transactionId INT AUTO_INCREMENT PRIMARY KEY,
    sender_account VARCHAR(20),
    receiver_account VARCHAR(20),
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type ENUM('Deposit', 'Withdrawal', 'Transfer') NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sender_account FOREIGN KEY (sender_account) REFERENCES Account(account_number) ON DELETE SET NULL,
    CONSTRAINT fk_receiver_account FOREIGN KEY (receiver_account) REFERENCES Account(account_number) ON DELETE SET NULL
);
