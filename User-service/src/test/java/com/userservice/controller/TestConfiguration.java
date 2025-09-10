package com.userservice.controller;

import com.userservice.mapper.UserMapper;
import org.springframework.context.annotation.Bean;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }
}
