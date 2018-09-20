package com.minowak.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String name;
    private String surname;
    private Set<Account> accounts;
}
