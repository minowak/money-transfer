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

public class UsersControllerTest {
    private final UsersService usersService = UsersService.getInstance();
    private final AccountsService accountsService = AccountsService.getInstance();

    private UsersController usersController;

    @Before
    public void setUp() {
        usersService.delete();
        accountsService.delete();

        usersController = new UsersController();
    }

    @Test
    public void shouldGetAccounts() {
        // Given
        User user = new User(1L, "testName", "testSurname");
        Account account = new Account("1234", BigInteger.ONE, user.getId());

        // When
        usersService.add(user);
        accountsService.add(account);
        Collection<Account> accounts = usersController.getAccounts(user.getId());

        // Then
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.contains(account));
    }

    @Test
    public void shouldDeleteAccounts() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1");
        User user2 = new User(2L, "testName2", "testSurname2");
        Account account1 = new Account("1234", BigInteger.ONE, user1.getId());
        Account account2 = new Account("5678", BigInteger.ONE, user2.getId());

        // When
        usersService.add(user1);
        usersService.add(user2);
        accountsService.add(account1);
        accountsService.add(account2);
        usersController.deleteAccounts(user1.getId());

        // Then
        assertEquals(accountsService.get().size(), 1);
        assertTrue(accountsService.get().contains(account2));
    }

    @Test
    public void shouldGetUsers() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1");
        User user2 = new User(2L, "testName2", "testSurname2");

        // When
        usersService.add(user1);
        usersService.add(user2);
        Collection<User> users = usersController.getUsers();

        // Then
        assertEquals(users.size(), 2);
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void shouldGetUser() {
        // Given
        User user = new User(1L, "testName", "testSurname");

        // When
        usersService.add(user);
        User resultUser = usersController.getUser(user.getId());

        // Then
        assertEquals(resultUser, user);
    }

    @Test
    public void shouldCreateUser() {
        // Given
        User user = new User(1L, "testName", "testSurname");

        // When
        usersController.createUser(user);

        // Then
        assertEquals(usersService.get(user.getId()), user);
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        User user = new User(1L, "testName", "testSurname");
        User updatedUser = user.toBuilder().name("updatedName").build();

        // When
        usersController.createUser(user);
        usersController.updateUser(updatedUser, user.getId());

        // Then
        assertEquals(usersService.get(user.getId()).getName(), updatedUser.getName());
    }

    @Test
    public void shouldDeleteAllUsers() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1");
        User user2 = new User(2L, "testName2", "testSurname2");
        Account account = new Account("1234", BigInteger.ONE, user1.getId());

        // When
        usersService.add(user1);
        usersService.add(user2);
        accountsService.add(account);
        usersController.deleteAllUsers();

        // Then
        assertEquals(usersService.get().size(), 0);
        assertEquals(accountsService.get().size(), 0);
    }

    @Test
    public void shouldDeleteUser() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1");
        User user2 = new User(2L, "testName2", "testSurname2");
        Account account = new Account("1234", BigInteger.ONE, user1.getId());

        // When
        usersService.add(user1);
        usersService.add(user2);
        accountsService.add(account);
        usersController.deleteUser(user1.getId());

        // Then
        assertEquals(usersService.get().size(), 1);
        assertEquals(accountsService.get().size(), 0);
        assertTrue(usersService.get().contains(user2));
    }
}