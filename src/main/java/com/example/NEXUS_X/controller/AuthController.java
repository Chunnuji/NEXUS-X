package com.example.NEXUS_X.controller;

import com.example.NEXUS_X.dto.LoginRequest;
import com.example.NEXUS_X.dto.LoginResponse;
import com.example.NEXUS_X.dto.SignupRequest;
import com.example.NEXUS_X.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse loginFunction(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/signup")
    public String signupFunction(@RequestBody SignupRequest signupRequest){
        System.out.println(">>> Sign CONTROLLER HIT <<<");
        return authService.signup(signupRequest);
    }

}
