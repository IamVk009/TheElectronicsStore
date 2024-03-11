package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.JwtRequest;
import com.lucifer.electronics.store.dtos.JwtResponse;
import com.lucifer.electronics.store.dtos.UserDto;
import com.lucifer.electronics.store.exceptions.BadApiRequestException;
import com.lucifer.electronics.store.security.JwtHelper;
import com.lucifer.electronics.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
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
}
