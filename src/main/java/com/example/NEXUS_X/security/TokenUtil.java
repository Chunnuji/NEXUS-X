package com.example.NEXUS_X.security;

import com.example.NEXUS_X.entity.UserInfo;

public interface TokenUtil {

    String generateJwtToken(UserInfo user);
    String getUserNameFromToken(String token);
    boolean validateToken(String token, UserInfo user);
}
