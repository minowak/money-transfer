package com.minowak.api;

import com.minowak.model.Account;
import com.minowak.model.Balance;
import com.minowak.model.Transfer;
import com.minowak.model.User;
import com.minowak.service.TransferService;
import com.minowak.service.UsersService;
import jersey.repackaged.com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UsersAccountsResourceTest {
    private UsersService usersService;
    private TransferService transferService;

    private UsersAccountsResource usersAccountsResource;

    @Before
    public void setUp() {
        usersService = new UsersService();
        transferService = new TransferService();
        usersAccountsResource = new UsersAccountsResource(usersService, transferService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetAccounts() {
        // Given
        Account account = new Account("1234");
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));

        // When
        usersService.add(user);
        Collection<Account> accounts = (Collection<Account>) usersAccountsResource.getAccounts(user.getId()).getEntity();

        // Then
        assertEquals(1, accounts.size());
        assertTrue(accounts.contains(account));
    }

    @Test
    public void shouldGetAccount() {
        // Given
        Account account = new Account("1234");
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));

        // When
        usersService.add(user);
        Account accountResponse = (Account) usersAccountsResource.getAccount(user.getId(), account.getNumber()).getEntity();

        // Then
        assertEquals(account, accountResponse);
    }

    @Test
    public void shouldCreateAccount() {
        // Given
        Account account = new Account("1234");
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
        Account account = new Account("1234");
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
        Account account1 = new Account("1234");
        Account account2 = new Account("5678");
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

        Balance balance1 = (Balance) usersAccountsResource.calculateBalance(user1.getId(), account1.getNumber()).getEntity();
        Balance balance2 = (Balance) usersAccountsResource.calculateBalance(user2.getId(), account2.getNumber()).getEntity();

        // Then
        assertEquals(BigInteger.valueOf(90), balance1.getValue());
        assertEquals(BigInteger.valueOf(110), balance2.getValue());
    }

    @Test
    public void shouldReturnErrorForBalanceForNonExistingUser() {
        // Given
        Account account1 = new Account("1234");
        Account account2 = new Account("5678");
        User user1 = new User(1L, "testName", "testSurname", Sets.newHashSet(account1));
        User user2 = new User(2L, "testName", "testSurname", Sets.newHashSet(account2));
        Transfer transfer0 = new Transfer(0L, null, account1.getNumber(), BigInteger.valueOf(200));
        Transfer transfer1 = new Transfer(1L, account1.getNumber(), account2.getNumber(), BigInteger.valueOf(100));
        Transfer transfer2 = new Transfer(2L, account1.getNumber(), account2.getNumber(), BigInteger.valueOf(10));

        // When
        usersService.add(user2);
        transferService.add(transfer0);
        transferService.add(transfer1);
        transferService.add(transfer2);

        Response response = usersAccountsResource.calculateBalance(user1.getId(), account1.getNumber());

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldReturnErrorForBalanceForNonExistingAccount() {
        // Given
        User user = new User(2L, "testName", "testSurname", Sets.newHashSet());

        // When
        usersService.add(user);
        Response response = usersAccountsResource.calculateBalance(user.getId(), "5678");

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

}