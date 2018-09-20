package com.minowak.service;

import com.google.common.collect.Sets;
import com.minowak.model.Account;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class AccountsService implements CrudService<String, Account> {
    private static volatile AccountsService INSTANCE = null;

    public static AccountsService getInstance() {
        if (INSTANCE == null) {
            synchronized (UsersService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AccountsService();
                }
            }
        }
        return INSTANCE;
    }

    private Set<Account> accounts = Collections.synchronizedSet(Sets.newHashSet());

    @Override
    public Account get(String number) {
        return accounts.stream().filter(a -> a.getNumber().equals(number)).findFirst().orElse(null);
    }

    @Override
    public Collection<Account> get() {
        return accounts;
    }

    @Override
    public boolean add(Account element) {
        Optional<Account> existingAccount = accounts.stream().filter(a -> a.getNumber().equals(element.getNumber())).findAny();
        return !existingAccount.isPresent() && accounts.add(element);
    }

    @Override
    public boolean add(Collection<Account> elements) {
        for (Account account : accounts) {
            if (accounts.stream().anyMatch(a -> a.getNumber().equals(account.getNumber()))) {
                return false;
            }
        }
        return accounts.addAll(elements);
    }

    @Override
    public boolean delete(String number) {
        Optional<Account> existingAccount = accounts.stream().filter(a -> a.getNumber().equals(number)).findFirst();
        return existingAccount.isPresent() && accounts.remove(existingAccount.get());
    }

    @Override
    public boolean delete() {
        accounts.clear();
        return true;
    }

    @Override
    public boolean update(String number, Account elementAfter) {
        Optional<Account> existingAccount = accounts.stream().filter(a -> a.getNumber().equals(number)).findFirst();
        if (existingAccount.isPresent()) {
            accounts.remove(existingAccount.get());
            accounts.add(elementAfter);
            return true;
        }
        return false;
    }
}
