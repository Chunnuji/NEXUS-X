package com.example.NEXUS_X.service;

import com.example.NEXUS_X.dto.LoginRequest;
import com.example.NEXUS_X.dto.LoginResponse;
import com.example.NEXUS_X.dto.SignupRequest;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    String signup(SignupRequest signupRequest);
}
