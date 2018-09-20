package com.minowak.model;

import lombok.*;

import java.math.BigInteger;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Account {
    private String number;
    private BigInteger balance;
    private Long userId;

    // TODO history of operations
}
