package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.LoginResponse;
import com.example.studentmanagement.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


//authcontroller -authentication manager - daoauthentication manager -
// custom user detail service -user repository -mysql -customuserdetails -
//bcrypt password match - successful
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
//I receive the login request in a DTO (LoginRequest),
// extract the username and password, create a UsernamePasswordAuthenticationToken
// with those credentials, and pass that token to the AuthenticationManager,
// which performs the actual authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // ity generates jwt token based on the algorithm given in jwtutil
        String token =
                jwtUtil.generateToken(request.getUsername());


        return LoginResponse.builder()
                .token(token)
                .build();
    }
}