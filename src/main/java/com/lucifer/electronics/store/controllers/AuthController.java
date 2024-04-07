package com.lucifer.electronics.store.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lucifer.electronics.store.dtos.JwtRequest;
import com.lucifer.electronics.store.dtos.JwtResponse;
import com.lucifer.electronics.store.dtos.UserDto;
import com.lucifer.electronics.store.entities.User;
import com.lucifer.electronics.store.exceptions.BadApiRequestException;
import com.lucifer.electronics.store.security.JwtHelper;
import com.lucifer.electronics.store.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
// Way - 2 : CORS Configuration
// Allowing cross-origin requests from applications running on all types of host. In our case its Angular Application which is running on 'http://localhost:4200'
//@CrossOrigin(origins="http://localhost:4200",
//             allowedHeaders = {"Authorization", "Accept", "Content-Type"},
//             allowCredentials = "true",
//             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//             maxAge = 3600)
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${client.google.id}")
    private String googleClientId;

    @Value("${password.user.new}")
    private String newPassword;

    /**
     * This method fetches details of the currently logged-in user, such as username, authorities, etc.,
     * and returns them as a ResponseEntity with an HTTP status of OK.
     *
     * @param principal: The Principal object represents the currently authenticated user.
     * @return
     */
    @GetMapping("/current/user")
    public ResponseEntity<UserDto> getLoggedInUserDetails(Principal principal) {
//      Retrieving the name of the currently authenticated user from the Principal object.
        String name = principal.getName();

//      Retrieving UserDetails for the logged-in user by invoking the loadUserByUsername method of a UserDetailsService implementation.
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        return new ResponseEntity<>(mapper.map(userDetails, UserDto.class), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtHelper.generateToken(userDetails);

        UserDto user = mapper.map(userDetails, UserDto.class);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userDto(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadApiRequestException("Invalid Username or Password..!");
        }
    }

//  Implementing Login with Google API controller

    /**
     * This method serves as an entry point for handling user authentication requests via Google OAuth.
     * It verifies the validity of the received ID token and extracts user information for further processing or
     * authentication within the application.
     *
     * @return JWTResponse
     */
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {
//      Fetching idToken from data coming from frontend Angular Application
        String idToken = data.get("idToken").toString();

//      Calling Google API to verify the idToken sent by client id is valid or not
//      We need to pass below class object in googleIdTokenVerifier.Builder class
        NetHttpTransport netHttpTransport = new NetHttpTransport();

        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
//      Calling Google API to verify the validity of the received ID token & also setting the audience for the verification process to the Google client ID specified in the googleClientId variable.
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
//      Using the above constructed verifier, the below method parses the received ID token and obtains its payload
        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
//      The payload contains information about the authenticated user, such as user ID, email,email_verified, picture,name and other relevant details.
        GoogleIdToken.Payload googleIdTokenPayload = googleIdToken.getPayload();

        log.info("Payload = " + googleIdTokenPayload);

        String email = googleIdTokenPayload.getEmail();

//      If user exists with above email id in our db, then return jwt token for that user otherwise create new user using the data received from google API.

        User user = userService.getUserByEmailForLoginWithGoogle(email).orElse(null);

        if(user == null){
            log.info("Since user doesn't exist so, creating new User");
            user = this.saveUser(email, data.get("name").toString(), data.get("photoUrl").toString());
        }

        ResponseEntity<JwtResponse> jwtResponseEntity = this.login(
                JwtRequest.builder()
                        .username(email)
                        .password(newPassword)
                        .build()
        );

        return jwtResponseEntity;
    }

    private User saveUser(String email, String name, String photoUrl) {
//      Creating new UserDto
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();
//      Saving new user into DB
        UserDto createdUser = userService.createUser(userDto);
        return this.mapper.map(createdUser, User.class);
    }
}
