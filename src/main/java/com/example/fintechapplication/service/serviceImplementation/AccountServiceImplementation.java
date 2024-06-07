package com.example.fintechapplication.service.serviceImplementation;

import com.example.fintechapplication.dto.AccountAmount;
import com.example.fintechapplication.dto.AccountName;
import com.example.fintechapplication.dto.AccountNameAmount;
import com.example.fintechapplication.dto.TransactionAmountTransactionAt;
import com.example.fintechapplication.entity.Account;
import com.example.fintechapplication.entity.Transaction;
import com.example.fintechapplication.exception.AccountNotFoundException;
import com.example.fintechapplication.exception.InsufficientBalanceException;
import com.example.fintechapplication.repository.AccountRepository;
import com.example.fintechapplication.repository.TransactionRepository;
import com.example.fintechapplication.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImplementation implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    @Override
    public AccountNameAmount openAccount(Account account) {
        accountRepository.save(account);
        return Optional.of(account).stream().map(account1 -> new AccountNameAmount(account1.getAccountName(), account.getBalance())).findFirst().get();
    }

    @Override
    public AccountNameAmount changeAccountName(Long accountId, AccountName newAccount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) throw new AccountNotFoundException("Account not found");
        accountOptional.get().setAccountName(newAccount.accountName());
        accountRepository.saveAndFlush(accountOptional.get());
        return accountOptional.stream().map(account -> new AccountNameAmount(account.getAccountName(), account.getBalance())).findFirst().get();
    }

    @Override
    @Transactional
    public Transaction deposit(Long accountId, AccountAmount accountAmount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.setBalance(account.getBalance() + accountAmount.amount());
        accountRepository.saveAndFlush(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(accountAmount.amount());
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction withdraw(Long accountId, AccountAmount accountAmount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (account.getBalance() < accountAmount.amount())
            throw new InsufficientBalanceException("Insufficient balance");
        double currentBalance = account.getBalance();
        account.setBalance(currentBalance - accountAmount.amount());
        accountRepository.saveAndFlush(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(-accountAmount.amount());
        return transactionRepository.save(transaction);
    }


    @Override
    public AccountNameAmount checkBalance(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) throw new AccountNotFoundException("Account not found");
        return account.stream().map(account1 -> new AccountNameAmount(account1.getAccountName(), account1.getBalance())).findFirst().get();
    }

    @Override
    public List<TransactionAmountTransactionAt> checkTransactions(Long accountId) {
        if (transactionRepository.findAllByAccount_Id(accountId).isEmpty())
            throw new AccountNotFoundException("Account Not Found");
        return transactionRepository.findAllByAccount_Id(accountId)
                .stream()
                .map(transaction -> new TransactionAmountTransactionAt(transaction.getAmount(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a").format(transaction.getTransactionAt())))
                .collect(Collectors.toList());

    }
}
