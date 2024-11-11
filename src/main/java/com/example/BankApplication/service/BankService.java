package com.example.BankApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BankService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public void depositMoney(String accountNumber, BigDecimal amount){
        accountService.updateBalance(accountNumber, amount);
        transactionService.recordDeposit(accountNumber, amount);
    }

    @Transactional
    public void withdrawMoney(String accountNumber, BigDecimal amount){
        BigDecimal currentBalance = accountService.getBalance(accountNumber);
        if (currentBalance.compareTo(amount) >= 0) {
            accountService.updateBalance(accountNumber, amount.negate());
            transactionService.recordWithdrawal(accountNumber, amount);
        }
    }

    @Transactional
    public void transfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount)  {
        BigDecimal currentBalance = accountService.getBalance(senderAccountNumber);
        if (currentBalance.compareTo(amount) >= 0) {
            accountService.updateBalance(senderAccountNumber, amount.negate());
            accountService.updateBalance(receiverAccountNumber, amount);
            transactionService.recordWithdrawal(senderAccountNumber, amount);
            transactionService.recordDeposit(receiverAccountNumber, amount);
            transactionService.sendToAnotherAccount(senderAccountNumber, receiverAccountNumber, amount);
        }
    }
}
