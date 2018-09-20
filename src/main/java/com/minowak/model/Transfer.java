package com.minowak.model;

import lombok.*;

import java.math.BigInteger;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = { "id" })
public class Transfer {
    private Long id;
    private String inputNumber;
    private String outputNumber;
    private BigInteger value;
}
