package com.lucifer.electronics.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;

    private String name;

    private String email;

    private String address;

    private String password;

    private String gender;

    private String about;

    private String imageName;
}
