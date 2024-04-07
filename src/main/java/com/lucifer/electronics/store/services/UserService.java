package com.lucifer.electronics.store.services;

import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.dtos.UserDto;
import com.lucifer.electronics.store.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto userDto);

    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection);

    UserDto getUserById(String userId);

    UserDto getUserByEmail(String email);

    UserDto updateUser(UserDto userDto, String userId);

    void deleteUser(String userId) throws IOException;

    List<UserDto> searchUser(String name);

    Optional<User> getUserByEmailForLoginWithGoogle(String email);
}
