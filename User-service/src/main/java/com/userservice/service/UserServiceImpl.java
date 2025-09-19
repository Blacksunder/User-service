package com.userservice.service;

import com.userservice.dto.MessageDto;
import com.userservice.dto.UserDto;
import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
import com.userservice.mapper.UserMapper;
import com.userservice.producer.MessageDtoKafkaSender;
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
    private final MessageDtoKafkaSender messageDtoKafkaSender;

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
            MessageDto messageDto = new MessageDto(userEntity.getEmail());
            messageDtoKafkaSender.sendCreationMessage(messageDto);
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
            foundEntity.setName(userDto.getName());
            foundEntity.setEmail(userDto.getEmail());
            foundEntity.setAge(userDto.getAge());
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
            MessageDto messageDto = new MessageDto(userEntity.getEmail());
            messageDtoKafkaSender.sendDeletionMessage(messageDto);
            return ResponseCode.OK;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }
}
