package com.example.BankApplication.dao;

import com.example.BankApplication.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionDAO {

    private final JdbcTemplate jdbcTemplate;

    public TransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Transaction> rowMapper = new RowMapper<Transaction>() {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(rs.getInt("transactionId"));
            transaction.setSenderAccount(rs.getString("sender_account"));
            transaction.setReceiverAccount(rs.getString("receiver_account"));
            transaction.setAmount(rs.getBigDecimal("amount"));
            transaction.setTransactionType(rs.getString("transaction_type"));
            transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
            return transaction;
        }
    };


    private static final String INSERT_TRANSACTION = "INSERT INTO Transaction (sender_account, receiver_account, amount, transaction_type) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_TRANSACTIONS = "SELECT * FROM Transaction";
    private static final String SELECT_TRANSACTION_BY_ID = "SELECT * FROM Transaction WHERE transactionId = ?";
    private static final String UPDATE_TRANSACTION = "UPDATE Transaction SET sender_account = ?, receiver_account = ?, amount = ?, transaction_type = ? WHERE transactionId = ?";
    private static final String DELETE_TRANSACTION = "DELETE FROM Transaction WHERE transactionId = ?";

    public List<Transaction> findTransactionHistoryByAccountNumber(String accountNumber) {
        String SELECT_TRANSACTIONS_BY_ACCOUNT_NUMBER = "SELECT * FROM Transaction WHERE sender_account = ? OR receiver_account = ?";
        return jdbcTemplate.query(SELECT_TRANSACTIONS_BY_ACCOUNT_NUMBER, rowMapper, accountNumber, accountNumber);
    }

    public int create(Transaction transaction) {
        return jdbcTemplate.update(
                INSERT_TRANSACTION,
                transaction.getSenderAccount(),
                transaction.getReceiverAccount(),
                transaction.getAmount(),
                transaction.getTransactionType()
        );
    }

    public List<Transaction> findAll() {
        return jdbcTemplate.query(SELECT_ALL_TRANSACTIONS, rowMapper);
    }

    public Transaction findById(int id) {
        return jdbcTemplate.queryForObject(SELECT_TRANSACTION_BY_ID, rowMapper, id);
    }

    public int update(Transaction transaction) {
        return jdbcTemplate.update(
                UPDATE_TRANSACTION,
                transaction.getSenderAccount(),
                transaction.getReceiverAccount(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionId()
        );
    }

    public int delete(int id) {
        return jdbcTemplate.update(DELETE_TRANSACTION, id);
    }
}
