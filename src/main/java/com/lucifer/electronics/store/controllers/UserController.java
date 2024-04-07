package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.ApiResponseMessage;
import com.lucifer.electronics.store.dtos.ImageResponseMessage;
import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.dtos.UserDto;
import com.lucifer.electronics.store.services.FileService;
import com.lucifer.electronics.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins="http://localhost:4200",
//             allowedHeaders = {"Authorization", "Accept", "Content-Type"},
//             allowCredentials = "true",
//             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//             maxAge = 3600)
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String uploadImagePath;

    //  Always send JSON as a response from controller instead of string and other type of data.
    @PostMapping("/create")
    public ResponseEntity<ApiResponseMessage> createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User Created Successfully.")
                .success(true)
                .build();
        logger.info("User Created Successfully...");
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(defaultValue = "0") int pageNumber,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(defaultValue = "userId") String sortBy,
                                                                 @RequestParam(defaultValue = "asc") String sortDirection) {
        PageableResponse<UserDto> allUsers = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDirection);
        logger.info("All Users Returned Successfully...");
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        UserDto user = userService.getUserById(userId);
        logger.info("User with Id = " + userId + " Returned Successfully...");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userDto, userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUserById(@PathVariable String userId) throws IOException {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User Deleted Successfully...")
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
    }

//  Upload User Image Controller
    @PostMapping(value = "/image/upload/{userId}")
    public ResponseEntity<ImageResponseMessage> uploadImageFile(@PathVariable String userId,
                                                                @RequestParam MultipartFile imageFile) throws IOException {
//      Fetching user based on userId
        UserDto user = userService.getUserById(userId);
//      Uploading imageFile
        String imageFileName = fileService.uploadImageFile(imageFile, uploadImagePath);
//      Updating existing user with newly uploaded imageName
        user.setImageName(imageFileName);
        UserDto userDto = userService.updateUser(user, userId);

        ImageResponseMessage responseMessage = ImageResponseMessage.builder()
                .imageFileName(imageFileName)
                .message("Image Uploaded Successfully..")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @GetMapping("/image/{userId}")
    public void serveImageFile(@PathVariable String userId, HttpServletResponse response) throws IOException {
//      Fetching user from its userId
        UserDto user = userService.getUserById(userId);
//      Get user image name from user to pass it as an argument to fileService Method
        String imageName = user.getImageName();
        logger.info("User Image Name : {}", imageName);
        InputStream imageFile = fileService.getImageFile(uploadImagePath, imageName);

//      Reading data from imageFile inputStream and adding it to response (Which requires HttpServletResponse)
//      Setting content type of response
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//      Copying data from imageFile inputStream to response
        StreamUtils.copy(imageFile, response.getOutputStream());
    }
}
