package com.example.PaymentService.Util;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class TransactionIdGenerator {

    @Bean
    public String generate() {
        return UUID.randomUUID().toString();
    }
}

