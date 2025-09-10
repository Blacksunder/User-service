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
        Mockito.when(mockUserRepository.findById("incorrect")).thenReturn(Optional.empty());

        UserEntity actual = userService.getUserById("incorrect");

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
        UserEntity user = new UserEntity();
        Mockito.when(mockUserRepository.save(user)).thenReturn(user);

        ResponseCode actualCode = userService.saveUser(user);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void updateUser_successUpdate() {
        Mockito.when(mockUserRepository.save(expected)).thenReturn(expected);
        Mockito.when(mockUserRepository.existsById(expected.getUuid())).thenReturn(true);

        ResponseCode actualCode = userService.updateUser(expected);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void updateUser_failUpdate() {
        UserEntity user = new UserEntity();
        Mockito.when(mockUserRepository.save(user)).thenReturn(user);

        ResponseCode actualCode = userService.updateUser(user);

        assertEquals(ResponseCode.ERROR, actualCode);
    }

    @Test
    public void deleteUser_successDelete() {
        UserEntity user = new UserEntity();

        ResponseCode actualCode = userService.deleteUser(user.getUuid());

        assertEquals(ResponseCode.OK, actualCode);
    }
}
