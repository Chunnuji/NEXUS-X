package com.example.AuthMicroservice.dao;

import com.example.AuthMicroservice.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

     Optional<UserInfo> findByUsername(String userName);
}
