package com.example.fintechapplication.controller;

import com.example.fintechapplication.dto.AccountAmount;
import com.example.fintechapplication.dto.AccountName;
import com.example.fintechapplication.dto.AccountNameAmount;
import com.example.fintechapplication.dto.TransactionAmountTransactionAt;
import com.example.fintechapplication.entity.Account;
import com.example.fintechapplication.entity.Transaction;
import com.example.fintechapplication.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
@Api(value = "Account management APIs")
public class AccountController {
    private final AccountService accountService;


    @PostMapping
    @ApiOperation(value = "Open a new account", response = AccountNameAmount.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountNameAmount openAccount(@RequestBody Account account) {
        return accountService.openAccount(account);
    }

    @PutMapping("{accountId}/change")
    @ApiOperation(value = "Change the account name", response = AccountNameAmount.class)
    public AccountNameAmount changeAccountName(@PathVariable Long accountId, @RequestBody AccountName newAccount) {
        return accountService.changeAccountName(accountId, newAccount);
    }


    @PostMapping("/{accountId}/deposit")
    @ApiOperation(value = "Deposit funds", response = AccountNameAmount.class)
    public Transaction deposit(@PathVariable Long accountId, @RequestBody AccountAmount amount) {
        return accountService.deposit(accountId, amount);
    }

    @PostMapping("/{accountId}/withdraw")
    @ApiOperation(value = "Withdraw funds", response = Transaction.class)
    public Transaction withdraw(@PathVariable Long accountId, @RequestBody AccountAmount amount) {
        return accountService.withdraw(accountId, amount);
    }

    @GetMapping("/{accountId}/balance")
    @ApiOperation(value = "Check balance", response = AccountNameAmount.class)
    public AccountNameAmount checkBalance(@PathVariable Long accountId) {
        return accountService.checkBalance(accountId);
    }

    @GetMapping("/{accountId}/transactions")
    @ApiOperation(value = "Check All Transaction")
    public List<TransactionAmountTransactionAt> checkTransactions(@PathVariable Long accountId) {
        return accountService.checkTransactions(accountId);
    }

}
