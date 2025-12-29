package com.example.NEXUS_X.service;

import com.example.NEXUS_X.dao.UserInfoRepository;
import com.example.NEXUS_X.dto.LoginRequest;
import com.example.NEXUS_X.dto.LoginResponse;
import com.example.NEXUS_X.dto.SignupRequest;
import com.example.NEXUS_X.entity.UserInfo;
import com.example.NEXUS_X.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    @Autowired
    public AuthServiceImpl(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, AuthUtil authUtil) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        UserInfo user = (UserInfo) authentication.getPrincipal();
        assert user != null;
        String token = authUtil.generateJwtToken(user);
        return new LoginResponse(token);
    }

    @Override
    public String signup(SignupRequest signupRequest) {
        Optional<UserInfo> user1 = userInfoRepository.findByUsername(signupRequest.getUsername());
        if (user1.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String pass = passwordEncoder.encode(signupRequest.getPassword());
        UserInfo user = new UserInfo(signupRequest.getUsername(),pass,Set.of("ROLE_USER"),true);
        userInfoRepository.save(user);

        return "Hi "+signupRequest.getUsername()+"!, you have signed up.";
    }
}
