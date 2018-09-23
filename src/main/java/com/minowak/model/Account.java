package com.minowak.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "number" })
@Builder(toBuilder = true)
public class Account {
    private String number;
}
