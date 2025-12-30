package com.example.AuthMicroservice.service;

import com.example.AuthMicroservice.dto.LoginRequest;
import com.example.AuthMicroservice.dto.LoginResponse;
import com.example.AuthMicroservice.dto.SignupRequest;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    String signup(SignupRequest signupRequest);
}
