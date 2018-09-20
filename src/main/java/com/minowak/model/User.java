package com.minowak.model;

import lombok.*;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
}
