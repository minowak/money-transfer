package com.minowak.service;

import com.minowak.model.Transfer;
import jersey.repackaged.com.google.common.collect.Sets;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class TransferService implements PersistenceService<Long, Transfer> {
    private Set<Transfer> transfers = Collections.synchronizedSet(Sets.newHashSet());

    @Override
    public Transfer get(Long id) {
        return transfers.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Transfer> get() {
        return transfers;
    }

    @Override
    public boolean add(Transfer element) {
        Optional<Transfer> existingTransfer = transfers.stream()
                .filter(t -> t.getId().equals(element.getId()))
                .findAny();
        Transfer newElement = element.getId() == null ? element.toBuilder().id(getMaxId() + 1).build()
                : element;
        return !existingTransfer.isPresent() && transfers.add(newElement);
    }

    @Override
    public boolean delete(Long id) {
        Optional<Transfer> existingTransfer = transfers.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
        return existingTransfer.isPresent() && transfers.remove(existingTransfer.get());
    }

    @Override
    public boolean delete() {
        transfers.clear();
        return true;
    }

    @Override
    public boolean update(Long id, Transfer elementAfter) {
        Optional<Transfer> existingTransfer = transfers.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
        if (existingTransfer.isPresent()) {
            transfers.remove(existingTransfer.get());
            transfers.add(elementAfter);
            return true;
        }
        return false;
    }

    private synchronized Long getMaxId() {
        Optional<Transfer> existingUser = transfers.stream().max(Comparator.comparing(Transfer::getId));
        return existingUser.map(Transfer::getId).orElse(0L);
    }
}
