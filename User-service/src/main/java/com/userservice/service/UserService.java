package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;

import java.util.List;

public interface UserService {

    UserDto getUserById(String uuid);

    List<String> getAllUsersId();

    ResponseCode saveUser(UserDto user);

    ResponseCode updateUser(UserDto user, String uuid);

    ResponseCode deleteUser(String uuid);

}
