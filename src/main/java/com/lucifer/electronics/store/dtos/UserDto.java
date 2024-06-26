package com.lucifer.electronics.store.dtos;

import com.lucifer.electronics.store.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min = 2, max = 20, message = "Name should be of minimum 2 and maximum 20 characters..")
    private String name;

    @NotBlank(message = "email is required..")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@([-a-zA-Z0-9]+\\.)+[a-zA-Z]{2,4}$", message = "Invalid user email..")
    private String email;

    @NotBlank(message = "Address is required..")
    private String address;

    @NotBlank(message = "Password is required..")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender..")
    private String gender;

    @NotBlank(message = "Write something about yourself..")
    private String about;

//  Validating image with Custom Validator
    @ImageNameValid(message = "Please enter valid image name")
    private String imageName;

    private Set<RoleDto> roles = new HashSet<>();
}
