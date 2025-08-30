package service;

import dto.UserDto;
import entity.UserEntity;
import enums.ResponseCode;

import java.util.List;

public interface UserServiceInterface {

    UserEntity getUserById(String uuid);

    List<UserEntity> getAllUsers();

    ResponseCode saveUser(UserEntity user);

    ResponseCode updateUser(UserEntity user);

    ResponseCode deleteUser(UserEntity user);

}
