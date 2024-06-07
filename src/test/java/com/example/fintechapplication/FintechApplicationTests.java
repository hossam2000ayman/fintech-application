package com.example.fintechapplication;

import com.example.fintechapplication.dto.AccountAmount;
import com.example.fintechapplication.dto.AccountName;
import com.example.fintechapplication.dto.AccountNameAmount;
import com.example.fintechapplication.entity.Account;
import com.example.fintechapplication.entity.Transaction;
import com.example.fintechapplication.exception.AccountNotFoundException;
import com.example.fintechapplication.exception.InsufficientBalanceException;
import com.example.fintechapplication.repository.AccountRepository;
import com.example.fintechapplication.repository.TransactionRepository;
import com.example.fintechapplication.service.serviceImplementation.AccountServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FintechApplicationTests {

    private final Account existingAccount = new Account(1L, "TestAccount", 100.0, new Date(), null);
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private AccountServiceImplementation accountService;

    @Test
    void testOpenAccount() {
        //Arrange
        Account account = new Account();
        account.setId(10L);
        account.setAccountName("TestAccount");
        account.setBalance(0.0);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        //Act
        AccountNameAmount result = accountService.openAccount(account);


        //Assert
        assertEquals("TestAccount", result.accountName());
        assertEquals(0.0, result.amount());


    }


    @Test
    void testChangeAccountName() {
        // Arrange
        Long accountId = 10L;
        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setAccountName("ExistingName");
        existingAccount.setBalance(0.0);

        AccountName newAccountName = new AccountName("NewName");

        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(existingAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(existingAccount);

        // Act
        AccountNameAmount result = accountService.changeAccountName(accountId, newAccountName);

        // Assert
        assertEquals("NewName", result.accountName());
        assertEquals(0.0, result.amount());
    }

    @Test
    void testDeposit() {
        // Arrange
        AccountAmount depositAmount = new AccountAmount(50.0);
        Transaction depositTransaction = new Transaction();
        depositTransaction.setAmount(depositAmount.amount());

        when(accountRepository.findById(existingAccount.getId())).thenReturn(Optional.of(existingAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(existingAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(depositTransaction);

        // Act
        Transaction result = accountService.deposit(existingAccount.getId(), depositAmount);

        // Assert
        assertEquals(150.0, existingAccount.getBalance());
        assertEquals(depositAmount.amount(), result.getAmount());
    }


    @Test
    void testWithdraw() {
        // Arrange
        AccountAmount withdrawAmount = new AccountAmount(50.0);
        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setAmount(-withdrawAmount.amount());

        when(accountRepository.findById(existingAccount.getId())).thenReturn(Optional.of(existingAccount));
        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(existingAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(withdrawTransaction);

        // Act
        Transaction result = accountService.withdraw(existingAccount.getId(), withdrawAmount);

        // Assert
        assertEquals(50.0, existingAccount.getBalance());
        assertEquals(-withdrawAmount.amount(), result.getAmount());
    }

    @Test
    void testWithdrawInsufficientBalance() {
        // Arrange
        AccountAmount withdrawAmount = new AccountAmount(200.0);

        when(accountRepository.findById(existingAccount.getId())).thenReturn(Optional.of(existingAccount));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.withdraw(existingAccount.getId(), withdrawAmount);
        });
    }

    @Test
    void testWithdrawNonExistingAccount() {
        // Arrange
        Long nonExistingAccountId = 999L;

        when(accountRepository.findById(nonExistingAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.withdraw(nonExistingAccountId, new AccountAmount(50.0));
        });
    }

    @Test
    void testCheckBalanceNonExistingAccount() {
        // Arrange
        Long nonExistingAccountId = 999L;

        when(accountRepository.findById(nonExistingAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.checkBalance(nonExistingAccountId);
        });
    }


    @Test
    void testCheckTransactionsNonExistingAccount() {
        // Arrange
        Long nonExistingAccountId = 999L;

        when(accountRepository.findById(nonExistingAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.checkTransactions(nonExistingAccountId);
        });
    }

    @Test
    void testDepositNonExistingAccount() {
        // Arrange
        Long nonExistingAccountId = 999L;
        AccountAmount depositAmount = new AccountAmount(50.0);

        when(accountRepository.findById(nonExistingAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.deposit(nonExistingAccountId, depositAmount);
        });
    }

    @Test
    void testChangeAccountNameNonExistingAccount() {
        // Arrange
        Long nonExistingAccountId = 999L;
        AccountName newAccountName = new AccountName("NewName");

        when(accountRepository.findById(nonExistingAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.changeAccountName(nonExistingAccountId, newAccountName);
        });
    }

}
