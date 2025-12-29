package com.example.NEXUS_X.service;

import com.example.NEXUS_X.dao.UserInfoRepository;
import com.example.NEXUS_X.entity.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoDetailService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoDetailService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        return userInfoRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
