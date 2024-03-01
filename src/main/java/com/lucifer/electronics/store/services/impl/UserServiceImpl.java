package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.dtos.UserDto;
import com.lucifer.electronics.store.entities.User;
import com.lucifer.electronics.store.exceptions.ResourceNotFoundException;
import com.lucifer.electronics.store.helper.Helper;
import com.lucifer.electronics.store.repositories.UserRepository;
import com.lucifer.electronics.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${user.profile.image.path}")
    private String uploadImagePath;

    @Override
    public void createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
//      Encoding password before creating user and storing it in DB.
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(dtoToEntity(userDto));
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
//      Implementation of findAll() method without pagination
//      List<User> users = userRepository.findAll();

//      Implementing pagination with sorting
//      Creating Sort object
        Sort sort = (sortDirection.equalsIgnoreCase("asc") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending()));
//      Creating pageable object using PageRequest implementation class of Pageable interface
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);
//      Getting list of usres from page.
//      List<User> users = page.getContent();

//      Elite Implementation - Converting list of type User to list of type UserDto Using stream api
//      List<UserDto> userDtoList = users.stream().map(user -> entityToDto(user)).toList();
        return Helper.getPageableResponse(page, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given Id " + userId + " does not exist."));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User with given email " + email + " does not exist."));
        return entityToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given Id " + userId + " does not exist."));

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
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with Id " + userId + " not found"));

        String imageName = user.getImageName();
        String imageFileDestination = uploadImagePath + imageName;
        logger.info("ImageFileDestination : {}", imageFileDestination);

        try {
//          Creating path to delete image from destination folder
            Path path = Paths.get(imageFileDestination);
//          Deleting image file
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.error("User Image does not exist in folder");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//      Deleting user
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
