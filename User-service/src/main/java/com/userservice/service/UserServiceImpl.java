package com.userservice.service;

import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
import com.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getUserById(String uuid) {
        return userRepository.findById(uuid).orElse(null);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Override
    public ResponseCode saveUser(UserEntity user) {
        try {
            userRepository.save(user);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }

    @Transactional
    @Override
    public ResponseCode updateUser(UserEntity user) {
        if (user == null || !userRepository.existsById(user.getUuid())) {
            return ResponseCode.ERROR;
        }
        try {
            userRepository.save(user);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }

    @Override
    public ResponseCode deleteUser(UserEntity user) {
        try {
            userRepository.delete(user);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }
}
