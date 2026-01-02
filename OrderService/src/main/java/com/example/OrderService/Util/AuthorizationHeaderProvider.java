package com.example.OrderService.Util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderProvider {

    private final HttpServletRequest request;

    public AuthorizationHeaderProvider(HttpServletRequest request) {
        this.request = request;
    }

    public String getAuthorizationHeader() {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}

