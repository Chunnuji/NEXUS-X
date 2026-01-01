package com.example.common.security;

import com.example.common.dto.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SharedJwtFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (authUtil.validateToken(token)) {
                Claims claims = authUtil.getClaims(token);

                // 1. Extract the specific 'userId' claim and other data from the token
                Integer userId = claims.get("userId", Integer.class);
                String username = claims.getSubject();

                // Map roles from the JWT (assuming they are stored as a List of Strings)
                List<String> rolesList = (List<String>) claims.get("roles");
                Set<String> rolesSet = rolesList != null ? new HashSet<>(rolesList) : Collections.emptySet();

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 2. Create the UserPrincipal record using the extracted ID
                    UserPrincipal principal = new UserPrincipal(userId, username, rolesSet);

                    // 3. Set the custom principal object into the Authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities() // Uses the overridden method in your record
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}