package com.minowak.api;

import com.google.common.collect.Sets;
import com.minowak.model.User;
import com.minowak.service.UsersService;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UsersResourceTest {
    private UsersService usersService;

    private UsersResource usersResource;

    @Before
    public void setUp() {
        usersService = new UsersService();
        usersResource = new UsersResource(usersService);
    }

    @Test
    public void shouldGetUsers() {
        // Given
        User user1 = new User(1L, "testName1", "testSurname1", Sets.newHashSet());
        User user2 = new User(2L, "testName2", "testSurname2", Sets.newHashSet());

        // When
        usersService.add(user1);
        usersService.add(user2);
        Collection<User> users = usersResource.getUsers();

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
        User resultUser = usersResource.getUser(user.getId());

        // Then
        assertEquals(user, resultUser);
    }

    @Test
    public void shouldCreateUser() {
        // Given
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet());

        // When
        usersResource.createUser(user);

        // Then
        assertEquals(user, usersService.get(user.getId()));
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        User user = new User(1L, "testName", "testSurname", Sets.newHashSet());
        User updatedUser = user.toBuilder().name("updatedName").build();

        // When
        usersResource.createUser(user);
        usersResource.updateUser(updatedUser, user.getId());

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
        usersResource.deleteAllUsers();

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
        usersResource.deleteUser(user1.getId());

        // Then
        assertEquals(1, usersService.get().size());
        assertTrue(usersService.get().contains(user2));
    }

}