package com.minowak.service;

import com.google.common.collect.Sets;
import com.minowak.model.User;

import java.util.*;

public final class UsersService implements PersistenceService<Long, User> {
    private static volatile UsersService INSTANCE = null;

    public static UsersService getInstance() {
        if (INSTANCE == null) {
            synchronized (UsersService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UsersService();
                }
            }
        }
        return INSTANCE;
    }

    private Set<User> users = Collections.synchronizedSet(Sets.newHashSet());

    @Override
    public User get(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Collection<User> get() {
        return users;
    }

    @Override
    public boolean add(User element) {
        Optional<User> existingUser = users.stream().filter(u -> u.getId().equals(element.getId())).findAny();
        return !existingUser.isPresent() && users.add(element.toBuilder().id(getMaxId() + 1).build());
    }

    // TODO also remove accounts
    @Override
    public boolean delete(Long id) {
        Optional<User> existingUser = users.stream().filter(u -> u.getId().equals(id)).findFirst();
        return existingUser.isPresent() && users.remove(existingUser.get());
    }

    @Override
    public boolean delete() {
        users.clear();
        return true;
    }

    @Override
    public boolean update(Long id, User elementAfter) {
        Optional<User> existingUser = users.stream().filter(u -> u.getId().equals(id)).findFirst();
        if (existingUser.isPresent()) {
            users.remove(existingUser.get());
            users.add(elementAfter);
            return true;
        }
        return false;
    }

    private synchronized Long getMaxId() {
        Optional<User> existingUser = users.stream().max(Comparator.comparing(User::getId));
        return existingUser.map(User::getId).orElse(0L);
    }
}
