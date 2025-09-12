package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
import com.userservice.mapper.UserMapper;
import com.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(String uuid) {
        UserEntity user = userRepository.findById(uuid).orElse(null);
        return userMapper.userEntityToDto(user);
    }

    @Override
    public List<String> getAllUsersId() {
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
        return userEntities.stream()
                .map(UserEntity::getUuid)
                .toList();
    }

    @Override
    public ResponseCode saveUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity(userDto.getName(),
                userDto.getEmail(), userDto.getAge());
        try {
            userRepository.save(userEntity);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }

    @Transactional
    @Override
    public ResponseCode updateUser(UserDto userDto, String uuid) {
        UserEntity foundEntity = userRepository.findById(uuid).orElse(null);
        if (foundEntity == null) {
            return ResponseCode.ERROR;
        }
        try {
            UserEntity newEntity = new UserEntity(userDto.getName(),
                    userDto.getEmail(), userDto.getAge());
            newEntity.setUuid(uuid);
            newEntity.setCreatedAt(newEntity.getCreatedAt());
            userRepository.save(newEntity);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }

    @Transactional
    @Override
    public ResponseCode deleteUser(String uuid) {
        UserEntity userEntity = userRepository.findById(uuid).orElse(null);
        if (userEntity == null) {
            return ResponseCode.ERROR;
        }
        try {
            userRepository.deleteById(uuid);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }
}
