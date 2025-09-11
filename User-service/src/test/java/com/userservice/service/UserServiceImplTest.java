package com.userservice.service;

import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
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
    private final UserService userService = new UserServiceImpl(mockUserRepository);
    private final LocalDateTime time = LocalDateTime.now();
    private final UserEntity expected = new UserEntity("100", "aaa", "bbb", 10, time);

    @Test
    public void getUserById_withCorrectId() {
        Mockito.when(mockUserRepository.findById(expected.getUuid())).thenReturn(Optional.of(expected));

        UserEntity actual = userService.getUserById(expected.getUuid());

        assertEquals(expected, actual);
    }

    @Test
    public void getUserById_withIncorrectId() {
        Mockito.when(mockUserRepository.findById(expected.getUuid())).thenReturn(Optional.empty());

        UserEntity actual = userService.getUserById(expected.getUuid());

        assertNull(actual);
    }

    @Test
    public void getAllUsers_notEmptyTable() {
        List<UserEntity> expectedList = List.of(expected, expected, expected);
        Mockito.when(mockUserRepository.findAll()).thenReturn(expectedList);

        List<UserEntity> actualList = userService.getAllUsers();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void getAllUsers_emptyTable() {
        Mockito.when(mockUserRepository.findAll()).thenReturn(List.of());

        List<UserEntity> actualList = userService.getAllUsers();

        assertEquals(0, actualList.size());
    }

    @Test
    public void saveUser_successSave() {
        Mockito.when(mockUserRepository.save(expected)).thenReturn(expected);

        ResponseCode actualCode = userService.saveUser(expected);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void updateUser_successUpdate() {
        Mockito.when(mockUserRepository.save(expected)).thenReturn(expected);
        Mockito.when(mockUserRepository.findById(any(String.class))).thenReturn(Optional.of(new UserEntity()));

        ResponseCode actualCode = userService.updateUser(expected);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void updateUser_failUpdate() {
        Mockito.when(mockUserRepository.save(expected)).thenReturn(expected);

        ResponseCode actualCode = userService.updateUser(expected);

        assertEquals(ResponseCode.ERROR, actualCode);
    }

    @Test
    public void deleteUser_successDelete() {
        Mockito.when(mockUserRepository.findById(any(String.class))).thenReturn(Optional.of(new UserEntity()));
        Mockito.doNothing().when(mockUserRepository).deleteById(any(String.class));

        ResponseCode actualCode = userService.deleteUser(expected.getUuid());

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void deleteUser_failDelete() {
        Mockito.when(mockUserRepository.findById(any(String.class))).thenReturn(Optional.empty());

        ResponseCode actualCode = userService.deleteUser(expected.getUuid());

        assertEquals(ResponseCode.ERROR, actualCode);
    }
}
