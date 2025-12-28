package com.example.NEXUS_X.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @GetMapping("/hello")
    public String helloFun(){
        return "Hello World!";
    }
}
