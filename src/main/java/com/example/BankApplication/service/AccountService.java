package com.example.BankApplication.service;

import com.example.BankApplication.dao.AccountDAO;
import com.example.BankApplication.exception.MinimumBalanceException;
import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountDAO accountDAO;

    @Autowired
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void updateBalance(String accountNumber, BigDecimal amount) {
        try {
            BigDecimal currentBalance = accountDAO.getBalanceByAccountNumber(accountNumber);
            BigDecimal newBalance = currentBalance.add(amount);
            accountDAO.updateBalanceByAccountNumber(accountNumber, newBalance);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Account does not exist with Account Number: " + accountNumber);
        }
    }

    public BigDecimal getBalance(String accountNumber) {
        try {
            return accountDAO.getBalanceByAccountNumber(accountNumber);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Account does not exist with Account Number: " + accountNumber);
        }
    }

    public String createAccount(Account account) {
        if (account.getAccountBalance() == null) {
            throw new IllegalArgumentException("Account balance cannot be null.");
        }

        switch (account.getAccountType()) {
            case "Savings":
                if (account.getAccountBalance().compareTo(new BigDecimal("1000")) < 0) {
                    throw new MinimumBalanceException("Minimum balance for Savings account is 1000 rupees.");
                }
                break;
            case "Business":
                if (account.getAccountBalance().compareTo(new BigDecimal("1000")) < 0) {
                    throw new MinimumBalanceException("Minimum balance for Business account is 1000 rupees.");
                }
                break;
            case "Checking":
                break;
            default:
                throw new IllegalArgumentException("Invalid account type: " + account.getAccountType());
        }

        String accountNumber = accountDAO.create(account);
        account.setAccountNumber(accountNumber);
        return accountNumber;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        try {
            return accountDAO.findByAccountNumber(accountNumber);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Account does not exist with Account Number: " + accountNumber);
        }
    }

    public Account updateAccount(Account account) {
        try {
            accountDAO.update(account);
            return account;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Account does not exist with Account Number: " + account.getAccountNumber());
        }
    }

    public void deleteAccount(String accountNumber) {
        try {
            accountDAO.deleteByAccountNumber(accountNumber);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Account does not exist with Account Number: " + accountNumber);
        }
    }
}
