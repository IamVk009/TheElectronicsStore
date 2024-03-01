package com.lucifer.electronics.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * This method fetches details of the currently logged-in user, such as username, authorities, etc.,
     * and returns them as a ResponseEntity with an HTTP status of OK.
     *
     * @param principal: The Principal object represents the currently authenticated user.
     * @return
     */
    @GetMapping("/current/user")
    public ResponseEntity<UserDetails> getLoggedInUserDetails(Principal principal) {
//      Retrieving the name of the currently authenticated user from the Principal object.
        String name = principal.getName();

//      Retrieving UserDetails for the logged-in user by invoking the loadUserByUsername method of a UserDetailsService implementation.
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }
}
