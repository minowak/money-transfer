package com.minowak.model;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Balance {
    private BigInteger value;
    private List<Transfer> history;
}
