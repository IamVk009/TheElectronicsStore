package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.UserDto;
import com.lucifer.electronics.store.entities.User;
import com.lucifer.electronics.store.repositories.UserRepository;
import com.lucifer.electronics.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userRepository.save(dtoToEntity(userDto));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
//      Elite Implementation - Converting list of type User to list of type UserDto Using stream api
        List<UserDto> userDtoList = users.stream().map(user -> entityToDto(user)).toList();
        return userDtoList;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with given Id " + userId + " does not exist."));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with given email " + email + " does not exist."));
        return entityToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with given Id " + userId + " does not exist."));

        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);
        return entityToDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with Id " + userId + " not found"));
        userRepository.delete(user);
    }

    public List<UserDto> searchUser(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        List<UserDto> userDtoList = users.stream().map(user -> entityToDto(user)).toList();
        return userDtoList;
    }


    private User dtoToEntity(UserDto userDto) {
//        return User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .password(userDto.getPassword())
//                .email(userDto.getEmail())
//                .address(userDto.getAddress())
//                .gender(userDto.getGender())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .build();

        return mapper.map(userDto, User.class);
    }

    private UserDto entityToDto(User user) {
//        return UserDto.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .password(user.getPassword())
//                .email(user.getEmail())
//                .address(user.getAddress())
//                .gender(user.getGender())
//                .about(user.getAbout())
//                .imageName(user.getImageName())
//                .build();

        return mapper.map(user, UserDto.class);
    }
}
