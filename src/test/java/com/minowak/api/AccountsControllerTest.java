package com.minowak.api;

import com.minowak.model.Account;
import com.minowak.model.User;
import com.minowak.service.AccountsService;
import com.minowak.service.UsersService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountsControllerTest {
    private final UsersService usersService = UsersService.getInstance();
    private final AccountsService accountsService = AccountsService.getInstance();

    private AccountsController accountsController;

    @Before
    public void setUp() {
        usersService.delete();
        accountsService.delete();
        accountsController = new AccountsController();
    }

    @Test
    public void shouldGetAccounts() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1");
        User user2 = new User(2L, "testName2", "testSurname2");
        Account account1 = new Account("1234", BigInteger.ONE, 1L);
        Account account2 = new Account("5678", BigInteger.ONE, 2L);

        // When
        usersService.add(user1);
        usersService.add(user2);
        accountsService.add(account1);
        accountsService.add(account2);
        Collection<Account> accounts = accountsController.getAccounts();

        // Then
        assertEquals(accounts.size(), 2);
        assertTrue(accounts.contains(account1));
        assertTrue(accounts.contains(account2));
    }

    @Test
    public void shouldGetAccount() {
        // Given
        User user = new User(1L, "testName1", "testSurname1");
        Account account = new Account("1234", BigInteger.ONE, 1L);

        // When
        usersService.add(user);
        accountsService.add(account);
        Account resultAccount = accountsController.getAccount(account.getNumber());

        // Then
        assertEquals(resultAccount, account);
    }

    @Test
    public void shouldCreateAccount() {
        // Given
        User user = new User(1L, "testName1", "testSurname1");
        Account account = new Account("1234", BigInteger.ONE, 1L);

        // When
        usersService.add(user);
        accountsController.createAccount(account);

        // Then
        assertEquals(accountsController.getAccounts().size(), 1);
        assertTrue(accountsController.getAccounts().contains(account));
    }

    @Test
    public void shouldUpdateAccount() {
        // Given
        User user = new User(1L, "testName1", "testSurname1");
        Account account = new Account("1234", BigInteger.ONE, 1L);
        Account updatedAccount = account.toBuilder().number("5678").build();

        // When
        usersService.add(user);
        accountsController.createAccount(account);
        accountsController.updateAccount(updatedAccount, account.getNumber());

        // Then
        assertEquals(accountsController.getAccounts().size(), 1);
        assertTrue(accountsController.getAccounts().contains(updatedAccount));
    }

    @Test
    public void shouldDeleteAllAccounts() {
        // Given
        User user = new User(1L, "testName1", "testSurname1");
        Account account1 = new Account("1234", BigInteger.ONE, 1L);
        Account account2 = new Account("5678", BigInteger.ONE, 1L);

        // When
        usersService.add(user);
        accountsService.add(account1);
        accountsService.add(account2);
        accountsController.deleteAllAccounts();

        // Then
        assertEquals(accountsController.getAccounts().size(), 0);
    }

    @Test
    public void shouldDeleteAccount() {
        // Given
        User user = new User(1L, "testName1", "testSurname1");
        Account account1 = new Account("1234", BigInteger.ONE, 1L);
        Account account2 = new Account("5678", BigInteger.ONE, 1L);

        // When
        usersService.add(user);
        accountsService.add(account1);
        accountsService.add(account2);
        accountsController.deleteAccount(account1.getNumber());

        // Then
        assertEquals(accountsController.getAccounts().size(), 1);
        assertTrue(accountsController.getAccounts().contains(account2));
    }
}