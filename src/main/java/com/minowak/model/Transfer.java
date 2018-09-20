package com.minowak.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Transfer {
    private String inputNumber;
    private String outputNumber;
    private BigInteger value;
}
