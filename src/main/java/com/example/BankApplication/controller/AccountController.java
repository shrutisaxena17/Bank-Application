package com.example.BankApplication.controller;

import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Account;
import com.example.BankApplication.response.AccountResponse;
import com.example.BankApplication.security.JwtService;
import com.example.BankApplication.service.AccountService;
import com.example.BankApplication.service.BankService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final BankService bankService;

    @Autowired
    public AccountController(AccountService accountService, BankService bankService) {
        this.accountService = accountService;
        this.bankService = bankService;
    }

    @Autowired
    JwtService jwtService;

    @GetMapping
    public List<Account> getAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<AccountResponse> newAccount(@RequestBody Account account) {
        String accountNumber = accountService.createAccount(account);
        AccountResponse accountResponse = new AccountResponse("This is your Account Number", accountNumber);
        return ResponseEntity.ok(accountResponse);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> findAccountByAccountNumber(@PathVariable String accountNumber) throws NotFoundException {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountNumber, @RequestBody Account account) throws NotFoundException {
        account.setAccountNumber(accountNumber);
        Account updatedAccount = accountService.updateAccount(account);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccountByAccountNumber(@PathVariable String accountNumber) throws NotFoundException {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalanceByAccountNumber(@PathVariable String accountNumber) throws NotFoundException {
        BigDecimal balance = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }


    @PostMapping("/{accountNumber}/deposit")
    //@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount) throws NotFoundException {
        bankService.depositMoney(accountNumber, amount);
        return ResponseEntity.ok("Deposit successful. New balance: " + accountService.getBalance(accountNumber));
    }
    

    @PostMapping("/{accountNumber}/withdraw")
    //@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount) throws NotFoundException {
        bankService.withdrawMoney(accountNumber, amount);
        return ResponseEntity.ok("Withdrawal successful. New balance: " + accountService.getBalance(accountNumber));
    }


    @PostMapping("/transfer")
    //@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> transfer(@RequestParam String senderAccountNumber,
                                           @RequestParam String receiverAccountNumber,
                                           @RequestParam BigDecimal amount) throws NotFoundException {
        bankService.transfer(senderAccountNumber, receiverAccountNumber, amount);
        return ResponseEntity.ok("Transfer successful.");
    }
}
