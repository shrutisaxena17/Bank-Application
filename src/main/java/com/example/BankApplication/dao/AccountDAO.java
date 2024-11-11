package com.example.BankApplication.dao;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@Repository
public class AccountDAO {

    private final JdbcTemplate jdbcTemplate;
    private final Random random = new Random();

    public AccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Account> rowMapper = new RowMapper<Account>() {
        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            Account account = new Account();
            account.setAccountId(rs.getInt("accountId"));
            account.setCustomerUniqueId(rs.getString("customer_unique_id"));
            account.setAccountNumber(rs.getString("account_number"));
            account.setAccountBalance(rs.getBigDecimal("accountBalance"));
            account.setAccountType(rs.getString("accountType"));
            account.setCurrency(rs.getString("currency"));
            account.setStatus(rs.getString("status"));
            account.setCreatedDate(rs.getTimestamp("createdDate").toLocalDateTime());
            account.setLastUpdated(rs.getTimestamp("lastUpdated").toLocalDateTime());
            return account;
        }
    };

    private static final String INSERT_ACCOUNT = "INSERT INTO Account (customer_unique_id, account_number, accountBalance, accountType, currency, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM Account";
    private static final String SELECT_ACCOUNT_BY_NUMBER = "SELECT * FROM Account WHERE account_number = ?";
    private static final String UPDATE_ACCOUNT = "UPDATE Account SET accountBalance = ?, accountType = ?, currency = ?, status = ? WHERE account_number = ?";
    private static final String DELETE_ACCOUNT_BY_NUMBER = "DELETE FROM Account WHERE account_number = ?";
    private static final String UPDATE_BALANCE_BY_NUMBER = "UPDATE Account SET accountBalance = ? WHERE account_number = ?";
    private static final String SELECT_BALANCE_BY_NUMBER = "SELECT accountBalance FROM Account WHERE account_number = ?";
    private static final String SELECT_ACCOUNT_NUMBER = "SELECT COUNT(*) FROM Account WHERE account_number = ?";

    public int updateBalanceByAccountNumber(String accountNumber, BigDecimal newBalance) {
        return jdbcTemplate.update(UPDATE_BALANCE_BY_NUMBER, newBalance, accountNumber);
    }

    public BigDecimal getBalanceByAccountNumber(String accountNumber) {
        return jdbcTemplate.queryForObject(SELECT_BALANCE_BY_NUMBER, BigDecimal.class, accountNumber);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%010d", random.nextInt(1000000000));
        } while (accountNumberExists(accountNumber));
        return accountNumber;
    }

    private boolean accountNumberExists(String accountNumber) {
        Integer count = jdbcTemplate.queryForObject(SELECT_ACCOUNT_NUMBER, Integer.class, accountNumber);
        return count != null && count > 0;
    }

    public String create(Account account) {
        String accountNumber = generateUniqueAccountNumber();
        account.setAccountNumber(accountNumber);
        jdbcTemplate.update(INSERT_ACCOUNT,
                account.getCustomerUniqueId(),
                account.getAccountNumber(),
                account.getAccountBalance(),
                account.getAccountType(),
                account.getCurrency(),
                account.getStatus());
                account.getCreatedDate();
                account.getLastUpdated();
        return accountNumber;
    }

    public List<Account> findAll() {
        return jdbcTemplate.query(SELECT_ALL_ACCOUNTS, rowMapper);
    }

    public Account findByAccountNumber(String accountNumber) {
        return jdbcTemplate.queryForObject(SELECT_ACCOUNT_BY_NUMBER, rowMapper, accountNumber);
    }

    public int update(Account account) {
        return jdbcTemplate.update(UPDATE_ACCOUNT,
                account.getAccountBalance(),
                account.getAccountType(),
                account.getCurrency(),
                account.getStatus(),
                account.getAccountNumber());
    }

    public int deleteByAccountNumber(String accountNumber) {
        return jdbcTemplate.update(DELETE_ACCOUNT_BY_NUMBER, accountNumber);
    }
}
