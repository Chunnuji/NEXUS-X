package com.example.NEXUS_X.service;

import com.example.NEXUS_X.dto.LoginRequest;
import com.example.NEXUS_X.dto.LoginResponse;
import com.example.NEXUS_X.dto.SignupRequest;

public interface AuthService {

    public LoginResponse login(LoginRequest loginRequest);

    public String signup(SignupRequest signupRequest);
}
