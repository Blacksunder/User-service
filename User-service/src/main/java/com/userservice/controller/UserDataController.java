package com.userservice.controller;

import com.userservice.dto.UserDto;
import com.userservice.dto.UserIdDto;
import com.userservice.enums.ResponseCode;
import com.userservice.hateaos.HateaosBuilder;
import com.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user-service")
public class UserDataController {

    private final UserService userService;
    private final HateaosBuilder hateaosBuilder;

    @Operation(summary = "Get all users' IDs")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<String> usersIds = userService.getAllUsersId();
        CollectionModel<EntityModel<UserIdDto>> entityModels = hateaosBuilder.userIdDtoCollectionModel(usersIds);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entityModels);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was found"),
            @ApiResponse(responseCode = "400", description = "User wasn't found")
    })
    @GetMapping("/user/{uuid}")
    public ResponseEntity<?> getById(@PathVariable("uuid") String uuid) {
        UserDto userDto = userService.getUserById(uuid);
        if (userDto == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User not found");
        }
        EntityModel<UserDto> entityModel = hateaosBuilder.userDtoEntityModel(uuid, userDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entityModel);
    }

    @Operation(summary = "Saving one user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was saved successfully"),
            @ApiResponse(responseCode = "400", description = "User wasn't saved due to DB problems")
    })
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserDto userDto) {
        ResponseCode responseCode = userService.saveUser(userDto);
        if (responseCode == ResponseCode.ERROR) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("DB problem");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User was saved successfully");
    }

    @Operation(summary = "Updating of existing user's info")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was updated"),
            @ApiResponse(responseCode = "400", description = "Updating problem")
    })
    @PatchMapping("/update/{uuid}")
    public ResponseEntity<?> update(@RequestBody UserDto userDto,
                                    @PathVariable("uuid") String uuid) {
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

    @Operation(summary = "Deleting user with specific ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was deleted"),
            @ApiResponse(responseCode = "400", description = "User wasn't found")
    })
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
