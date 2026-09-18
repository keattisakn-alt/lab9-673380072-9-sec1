package com.example.lab9;

import com.example.lab9.model.Account;
import com.example.lab9.model.DepositTransaction;
import com.example.lab9.repository.AccountRepository;
import com.example.lab9.repository.DepositRepository;
import com.example.lab9.service.AccountService;
import com.example.lab9.service.DepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Lab9ApplicationTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DepositRepository depositRepository;

    @BeforeEach
    void setUp() {
        depositRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void testCreateAccount() {
        Account account = new Account("1234567890", "Keattisak Nantharat", 0.0);
        Account saved = accountService.createAccount(account);

        assertNotNull(saved.getId());
        assertEquals("1234567890", saved.getAccountNumber());
        assertEquals("Keattisak Nantharat", saved.getOwnerName());
        assertEquals(0.0, saved.getBalance());
    }

    @Test
    void testDepositSuccessful() {
        Account account = new Account("1234567890", "Keattisak Nantharat", 0.0);
        Account saved = accountService.createAccount(account);

        depositService.deposit(saved.getId(), 1000.0);

        Account updated = accountRepository.findById(saved.getId()).orElseThrow();
        assertEquals(1000.0, updated.getBalance());

        List<DepositTransaction> transactions = depositRepository.findAll();
        assertEquals(1, transactions.size());
        assertEquals(1000.0, transactions.get(0).getAmount());
        assertEquals(saved.getId(), transactions.get(0).getAccount().getId());
    }
}
