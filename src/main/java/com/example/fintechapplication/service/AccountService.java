package com.example.fintechapplication.service;

import com.example.fintechapplication.dto.*;
import com.example.fintechapplication.entity.Account;
import com.example.fintechapplication.entity.Transaction;

import java.util.List;

public interface AccountService {
    AccountNameAmount openAccount(Account account);

    AccountNameAmount changeAccountName(Long accountId, AccountName newAccount);


    Transaction deposit(Long accountId, AccountAmount amount);

    Transaction withdraw(Long accountId, AccountAmount amount);

    AccountNameAmount checkBalance(Long accountId);

    AccountNameTransaction checkTransactions(Long accountId);

    List<Account> checkAllAccounts();

}
