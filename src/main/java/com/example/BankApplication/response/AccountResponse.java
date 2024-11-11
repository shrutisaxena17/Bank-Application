package com.example.BankApplication.response;

public class AccountResponse {
    String message;
    String accountNumber;

    public AccountResponse(String message, String accountNumber) {
        this.message = message;
        this.accountNumber = accountNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
