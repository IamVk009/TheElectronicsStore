package com.lucifer.electronics.store.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
/*
   @Tag annotation is used to provide additional information about tags in the Swagger documentation.
   It can be applied at class or method level and is used to categorize and organize them in a meaningful way.
*/
@Tag(name = "Home Controller")
@SecurityRequirement(name = "schemer")
public class HomeController {

    @GetMapping
    public ResponseEntity<String> Test() {
        return new ResponseEntity<>("The Electronics Store", HttpStatus.OK);
    }

}
