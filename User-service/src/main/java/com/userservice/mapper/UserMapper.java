package com.userservice.mapper;

import com.userservice.dto.UserDto;
import com.userservice.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userEntityToDto(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getName(), user.getEmail(), user.getAge());
    }
}
