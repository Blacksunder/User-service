package com.userservice.controller;

import com.userservice.dto.UserDto;
import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
import com.userservice.mapper.UserMapper;
import com.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-service")
public class UserDataController {
    private final UserService userService;
    private final UserMapper userMapper;
    
    @Autowired
    public UserDataController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<String> usersIds = userService.getAllUsers().stream()
                .map(UserEntity::getUuid)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersIds);
    }

    @GetMapping("/user/{uuid}")
    public ResponseEntity<?> getById(@PathVariable("uuid") String uuid) {
        UserEntity userEntity = userService.getUserById(uuid);
        if (userEntity == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
        UserDto userDto = userMapper.userEntityToDto(userEntity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserDto userDto) {
        UserEntity userEntity = new UserEntity(userDto.getName(),
                userDto.getEmail(), userDto.getAge());
        ResponseCode responseCode = userService.saveUser(userEntity);
        if (responseCode == ResponseCode.ERROR) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User already exists");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User was saved successfully");
    }

    @PatchMapping("/update/{uuid}")
    public ResponseEntity<?> update(@RequestBody UserDto userDto, @PathVariable("uuid") String uuid) {
        UserEntity userEntity = new UserEntity(userDto.getName(),
                userDto.getEmail(), userDto.getAge());
        userEntity.setUuid(uuid);
        ResponseCode responseCode = userService.updateUser(userEntity);
        if (responseCode == ResponseCode.ERROR) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User doesn't exist");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User was updated successfully");
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> delete(@PathVariable("uuid") String uuid) {
        ResponseCode responseCode = userService.deleteUser(uuid);
        if (responseCode == ResponseCode.ERROR) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User doesn't exists");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User was deleted successfully");
    }
}
