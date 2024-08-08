package org.example.userservice.controller;

import org.example.userservice.model.User;
import org.example.userservice.service.AuthenticationService;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService; // Service for handling authentication

    @Autowired
    private UserService userService;



    // Endpoint to register a new user
    @PostMapping("/sign-up") //
    public ResponseEntity<?> signUp(@RequestBody User user) {
        // Check if the username already exists
        if (userService.findBUsername(user.getUsername()).isPresent()) {
            // If user exists, return HTTP 409 Conflict
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // If user does not exist, save the user and return HTTP 201 Created
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }



    // Endpoint to sign in and get a JWT
    @PostMapping("/sign-in") // URL: /api/authentication/sign-in
    public ResponseEntity<?> signIn(@RequestBody User user) {
        // Authenticate the user and return a JWT
        return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
    }
}
