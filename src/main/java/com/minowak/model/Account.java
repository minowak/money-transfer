package com.minowak.model;

import com.minowak.service.TransferService;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "number" })
@Builder(toBuilder = true)
public class Account {
    private String number;
    private Set<Transfer> transfers;

    private final TransferService transferService = TransferService.getInstance();

    public BigInteger getBalance() {
        Set<Transfer> accountTransfers = transferService.get().stream()
                .filter(t -> t.getInputNumber().equals(number) || t.getOutputNumber().equals(number))
                .collect(Collectors.toSet());
        BigInteger balance = BigInteger.ZERO;

        for (Transfer transfer : accountTransfers) {
            if (transfer.getInputNumber().equals(number)) {
                balance = balance.subtract(transfer.getValue());
            } else {
                balance = balance.add(transfer.getValue());
            }
        }

        return balance;
    }
}
