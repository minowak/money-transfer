package com.minowak.api;

import com.google.common.collect.Sets;
import com.minowak.model.Account;
import com.minowak.model.Balance;
import com.minowak.model.Transfer;
import com.minowak.model.User;
import com.minowak.service.TransferService;
import com.minowak.service.UsersService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UsersAccountsResourceTest {
    private final UsersService usersService = UsersService.getInstance();
    private final TransferService transferService = TransferService.getInstance();

    private UsersAccountsResource usersAccountsResource;

    @Before
    public void setUp() {
        usersService.delete();
        usersAccountsResource = new UsersAccountsResource();
    }

    @Test
    public void shouldGetAccounts() {
        // Given
        Account account = new Account("1234", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));

        // When
        usersService.add(user);
        Collection<Account> accounts = usersAccountsResource.getAccounts(user.getId());

        // Then
        assertEquals(1, accounts.size());
        assertTrue(accounts.contains(account));
    }

    @Test
    public void shouldGetAccount() {
        // Given
        Account account = new Account("1234", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));

        // When
        usersService.add(user);
        Account accountResponse = usersAccountsResource.getAccount(user.getId(), account.getNumber());

        // Then
        assertEquals(account, accountResponse);
    }

    @Test
    public void shouldCreateAccount() {
        // Given
        Account account = new Account("1234", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet());

        // When
        usersService.add(user);
        usersAccountsResource.createAccount(account, user.getId());

        // Then
        assertEquals(1, usersService.get().size());
        assertEquals(1, usersService.get().iterator().next().getAccounts().size());
        assertTrue(usersService.get().iterator().next().getAccounts().contains(account));
    }

    @Test
    public void shouldDeleteAccounts() {
        // Given
        Account account = new Account("1234", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));

        // When
        usersService.add(user);
        usersAccountsResource.deleteAccounts(user.getId());

        // Then
        assertEquals(1, usersService.get().size());
        assertEquals(0, usersService.get().iterator().next().getAccounts().size());
    }

    @Test
    public void shouldCalculateBalanceCorrectly() {
        // Given
        Account account1 = new Account("1234", Sets.newHashSet());
        Account account2 = new Account("5678", Sets.newHashSet());
        User user1 = new User(1L, "testName", "testSurname", Sets.newHashSet(account1));
        User user2 = new User(2L, "testName", "testSurname", Sets.newHashSet(account2));
        Transfer transfer0 = new Transfer(0L, null, account1.getNumber(), BigInteger.valueOf(200));
        Transfer transfer1 = new Transfer(1L, account1.getNumber(), account2.getNumber(), BigInteger.valueOf(100));
        Transfer transfer2 = new Transfer(2L, account1.getNumber(), account2.getNumber(), BigInteger.valueOf(10));

        // When
        usersService.add(user1);
        usersService.add(user2);
        transferService.add(transfer0);
        transferService.add(transfer1);
        transferService.add(transfer2);

        Balance balance1 = usersAccountsResource.calculateBalance(user1.getId(), account1.getNumber());
        Balance balance2 = usersAccountsResource.calculateBalance(user2.getId(), account2.getNumber());

        // Then
        assertEquals(BigInteger.valueOf(90), balance1.getValue());
        assertEquals(BigInteger.valueOf(110), balance2.getValue());
    }

}