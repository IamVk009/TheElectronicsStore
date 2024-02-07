package com.lucifer.electronics.store.services;

import com.lucifer.electronics.store.dtos.UserDto;
import com.lucifer.electronics.store.entities.User;

import java.util.List;

public interface UserService {
    void createUser(UserDto userDto);

    List<UserDto> getAllUsers(int pageNumber, int pageSize);

    UserDto getUserById(String userId);

    UserDto getUserByEmail(String email);

    UserDto updateUser(UserDto userDto, String userId);

    void deleteUser(String userId);

    List<UserDto> searchUser(String name);
}
