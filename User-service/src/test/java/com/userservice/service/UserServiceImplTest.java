package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
import com.userservice.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceImplTest {
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserServiceImpl(mockUserRepository, new UserMapper());
    private final LocalDateTime time = LocalDateTime.now();
    private final String uuid = "111";
    private final UserDto expectedDto = new UserDto("aaa", "bbb", 10);
    private final UserEntity expectedEntity = new UserEntity(uuid, "aaa", "bbb", 10, time);


    @Test
    public void getUserById_withCorrectId() {
        Mockito.when(mockUserRepository.findById(uuid)).thenReturn(Optional.of(expectedEntity));

        UserDto actual = userService.getUserById(uuid);

        assertEquals(expectedDto, actual);
    }

    @Test
    public void getUserById_withIncorrectId() {
        Mockito.when(mockUserRepository.findById(uuid)).thenReturn(Optional.empty());

        UserDto actual = userService.getUserById(uuid);

        assertNull(actual);
    }

    @Test
    public void getAllUsersId_notEmptyTable() {
        List<UserEntity> entityList = List.of(expectedEntity, expectedEntity);
        Mockito.when(mockUserRepository.findAll()).thenReturn(entityList);
        List<String> idList = entityList.stream().map(UserEntity::getUuid).toList();

        List<String> actualList = userService.getAllUsersId();

        assertEquals(idList, actualList);
    }

    @Test
    public void getAllUsersId_emptyTable() {
        Mockito.when(mockUserRepository.findAll()).thenReturn(List.of());

        List<String> actualList = userService.getAllUsersId();

        assertEquals(0, actualList.size());
    }

    @Test
    public void saveUser_successSave() {
        Mockito.when(mockUserRepository.save(any(UserEntity.class))).thenReturn(expectedEntity);

        ResponseCode actualCode = userService.saveUser(expectedDto);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void updateUser_successUpdate() {
        Mockito.when(mockUserRepository.save(any(UserEntity.class))).thenReturn(expectedEntity);
        Mockito.when(mockUserRepository.findById(any(String.class))).thenReturn(Optional.of(new UserEntity()));

        ResponseCode actualCode = userService.updateUser(expectedDto, uuid);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void updateUser_failUpdate() {
        Mockito.when(mockUserRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ResponseCode actualCode = userService.updateUser(expectedDto, uuid);

        assertEquals(ResponseCode.ERROR, actualCode);
    }

    @Test
    public void deleteUser_successDelete() {
        Mockito.when(mockUserRepository.findById(any(String.class))).thenReturn(Optional.of(new UserEntity()));
        Mockito.doNothing().when(mockUserRepository).deleteById(any(String.class));

        ResponseCode actualCode = userService.deleteUser(uuid);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void deleteUser_failDelete() {
        Mockito.when(mockUserRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ResponseCode actualCode = userService.deleteUser(uuid);

        assertEquals(ResponseCode.ERROR, actualCode);
    }
}
