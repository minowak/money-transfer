package com.minowak.model;

import lombok.*;

import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "number" })
@Builder(toBuilder = true)
public class Account {
    private String number;
    private BigInteger balance;
    private Set<Transfer> transfers;
}
