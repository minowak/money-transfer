package com.minowak.service;

import com.minowak.model.User;
import jersey.repackaged.com.google.common.collect.Sets;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public final class UsersService implements PersistenceService<Long, User> {
    private Set<User> users = Collections.synchronizedSet(Sets.newHashSet());

    @Override
    public User get(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<User> get() {
        return users;
    }

    @Override
    public boolean add(User element) {
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId().equals(element.getId()))
                .findAny();
        User newElement = element.getId() == null ? element.toBuilder().id(getMaxId() + 1).build()
                : element;
        return !existingUser.isPresent() && users.add(newElement);
    }

    @Override
    public boolean delete(Long id) {
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        return existingUser.isPresent() && users.remove(existingUser.get());
    }

    @Override
    public boolean delete() {
        users.clear();
        return true;
    }

    @Override
    public boolean update(Long id, User elementAfter) {
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
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
