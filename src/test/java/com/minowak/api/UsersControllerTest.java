package com.minowak.api;

import com.google.common.collect.Sets;
import com.minowak.model.Account;
import com.minowak.model.User;
import com.minowak.service.UsersService;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UsersControllerTest {
    private final UsersService usersService = UsersService.getInstance();

    private UsersController usersController;

    @Before
    public void setUp() {
        usersService.delete();
        usersController = new UsersController();
    }

    @Test
    public void shouldGetAccounts() {
        // Given
        Account account = new Account("1234", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));

        // When
        usersService.add(user);
        Collection<Account> accounts = usersController.getAccounts(user.getId());

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
        Account accountResponse = usersController.getAccount(user.getId(), account.getNumber());

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
        usersController.createAccount(account, user.getId());

        // Then
        assertEquals(1, usersService.get().size());
        assertEquals(1, usersService.get().iterator().next().getAccounts().size());
        assertTrue(usersService.get().iterator().next().getAccounts().contains(account));
    }

    @Test
    public void shouldUpdateAccount() {
        // Given
        Account accountBefore = new Account("1234", Sets.newHashSet());
        Account accountAfter = new Account("5678", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(accountBefore));

        // When
        usersService.add(user);
        usersController.updateAccount(accountAfter, user.getId(), accountBefore.getNumber());

        // Then
        assertEquals(1, usersService.get().size());
        assertEquals(1, usersService.get().iterator().next().getAccounts().size());
        assertTrue(usersService.get().iterator().next().getAccounts().contains(accountAfter));
    }

    @Test
    public void shouldDeleteAccounts() {
        // Given
        Account account = new Account("1234", Sets.newHashSet());
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet(account));

        // When
        usersService.add(user);
        usersController.deleteAccounts(user.getId());

        // Then
        assertEquals(1, usersService.get().size());
        assertEquals(0, usersService.get().iterator().next().getAccounts().size());
    }

    @Test
    public void shouldGetUsers() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1", Sets.newHashSet());
        User user2 = new User(2L, "testName2", "testSurname2", Sets.newHashSet());

        // When
        usersService.add(user1);
        usersService.add(user2);
        Collection<User> users = usersController.getUsers();

        // Then
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void shouldGetUser() {
        // Given
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet());

        // When
        usersService.add(user);
        User resultUser = usersController.getUser(user.getId());

        // Then
        assertEquals(user, resultUser);
    }

    @Test
    public void shouldCreateUser() {
        // Given
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet());

        // When
        usersController.createUser(user);

        // Then
        assertEquals(user, usersService.get(user.getId()));
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet());
        User updatedUser = user.toBuilder().name("updatedName").build();

        // When
        usersController.createUser(user);
        usersController.updateUser(updatedUser, user.getId());

        // Then
        assertEquals(updatedUser.getName(), usersService.get(user.getId()).getName());
    }

    @Test
    public void shouldDeleteAllUsers() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1", Sets.newHashSet());
        User user2 = new User(2L, "testName2", "testSurname2", Sets.newHashSet());

        // When
        usersService.add(user1);
        usersService.add(user2);
        usersController.deleteAllUsers();

        // Then
        assertEquals(0, usersService.get().size());
    }

    @Test
    public void shouldDeleteUser() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1", Sets.newHashSet());
        User user2 = new User(2L, "testName2", "testSurname2", Sets.newHashSet());

        // When
        usersService.add(user1);
        usersService.add(user2);
        usersController.deleteUser(user1.getId());

        // Then
        assertEquals(1, usersService.get().size());
        assertTrue(usersService.get().contains(user2));
    }
}