package com.minowak.service;

import com.google.common.collect.Lists;
import com.minowak.model.User;

import java.util.List;
import java.util.Optional;

public final class UsersService implements CrudService<User> {
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

    private List<User> users = Lists.newArrayList();

    public User get(Long id) {
        return users.get(id.intValue());
    }

    public List<User> get() {
        return users;
    }

    public boolean add(User element) {
        // TODO error codes
        Optional<Long> existingElementId = users.stream().map(User::getId).filter(id -> id.equals(element.getId())).findAny();
        return !existingElementId.isPresent() && users.add(element);
    }

    public boolean add(List<User> elements) {
        // TODO check if ids exists
        return users.addAll(elements);
    }

    public void delete(Long id) {
        users.remove(id.intValue());
    }

    public void delete() {
        users.clear();
    }

    public void update(Long id, User elementAfter) {
        users.add(id.intValue(), elementAfter);
    }
}
