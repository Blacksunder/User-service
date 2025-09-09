package com.userservice.service;

import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;

import java.util.List;

public interface UserService {

    UserEntity getUserById(String uuid);

    List<UserEntity> getAllUsers();

    ResponseCode saveUser(UserEntity user);

    ResponseCode updateUser(UserEntity user);

    ResponseCode deleteUser(UserEntity user);

}
