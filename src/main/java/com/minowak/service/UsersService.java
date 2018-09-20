package com.minowak.service;

import com.google.common.collect.Sets;
import com.minowak.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public final class UsersService implements CrudService<Long, User> {
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

    private Set<User> users = Sets.newHashSet();

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
        return !existingUser.isPresent() && users.add(element);
    }

    @Override
    public boolean add(Collection<User> elements) {
        for (User element : elements) {
            if (users.stream().anyMatch(u -> u.getId().equals(element.getId()))) {
                return false;

            }
        }
        return users.addAll(elements);
    }

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
}
