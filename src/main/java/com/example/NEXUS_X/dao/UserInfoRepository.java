package com.example.NEXUS_X.dao;

import com.example.NEXUS_X.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

     Optional<UserInfo> findByUsername(String userName);
}
