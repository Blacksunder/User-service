package com.userservice.controller;

import com.userservice.dto.UserDto;
import com.userservice.enums.ResponseCode;
import com.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user-service")
public class UserDataController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<String> usersIds = userService.getAllUsersId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersIds);
    }

    @GetMapping("/user/{uuid}")
    public ResponseEntity<?> getById(@PathVariable("uuid") String uuid) {
        UserDto userDto = userService.getUserById(uuid);
        if (userDto == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User not found");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserDto userDto) {
        ResponseCode responseCode = userService.saveUser(userDto);
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
        ResponseCode responseCode = userService.updateUser(userDto, uuid);
        if (responseCode == ResponseCode.ERROR) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
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
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User doesn't exists");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User was deleted successfully");
    }
}
