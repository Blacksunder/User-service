package com.userservice.service;

import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
import com.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
    public ResponseCode deleteUser(String uuid) {
        try {
            userRepository.deleteById(uuid);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }
}
