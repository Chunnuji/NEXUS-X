package com.example.AuthMicroservice.security;

import com.example.AuthMicroservice.entity.UserInfo;

public interface TokenUtil {

    String generateJwtToken(UserInfo user);
    String getUserNameFromToken(String token);
    boolean validateToken(String token, UserInfo user);
}
