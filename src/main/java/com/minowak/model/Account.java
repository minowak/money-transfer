package com.minowak.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter
@Setter
public class Account {
    private String number;
    private BigInteger balance;
    private Long userId;

    // TODO history of operations
}
