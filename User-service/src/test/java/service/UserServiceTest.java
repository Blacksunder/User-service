package service;

import entity.UserEntity;
import enums.ResponseCode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.UserDaoInterface;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserDaoInterface mockUserDao = Mockito.mock(UserDaoInterface.class);
    private final UserServiceInterface userService = new UserService(mockUserDao);
    private final LocalDateTime time = LocalDateTime.now();
    private final UserEntity expected = new UserEntity("100", "aaa", "bbb", 10, time);

    @Test
    public void getUserById_withCorrectId() {
        Mockito.when(mockUserDao.getUserById(expected.getUuid())).thenReturn(expected);

        UserEntity actual = userService.getUserById(expected.getUuid());

        assertEquals(expected, actual);
    }

    @Test
    public void getUserById_withIncorrectId() {
        Mockito.when(mockUserDao.getUserById("")).thenReturn(null);

        UserEntity actual = userService.getUserById("");

        assertNull(actual);
    }

    @Test
    public void getAllUsers_notEmptyTable() {
        List<UserEntity> expectedList = List.of(expected, expected, expected);
        Mockito.when(mockUserDao.getAllUsers()).thenReturn(expectedList);

        List<UserEntity> actualList = userService.getAllUsers();

        assertEquals(expectedList, actualList);
    }

    @Test
    public void getAllUsers_emptyTable() {
        Mockito.when(mockUserDao.getAllUsers()).thenReturn(List.of());

        List<UserEntity> actualList = userService.getAllUsers();

        assertEquals(0, actualList.size());
    }

    @Test
    public void saveUser_successSave() {
        UserEntity user = new UserEntity();
        Mockito.when(mockUserDao.saveUser(user)).thenReturn(ResponseCode.OK);

        ResponseCode actualCode = userService.saveUser(user);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void saveUser_failSave() {
        UserEntity user = new UserEntity();
        Mockito.when(mockUserDao.saveUser(user)).thenReturn(ResponseCode.ERROR);

        ResponseCode actualCode = userService.saveUser(user);

        assertEquals(ResponseCode.ERROR, actualCode);
    }

    @Test
    public void updateUser_successUpdate() {
        UserEntity user = new UserEntity();
        Mockito.when(mockUserDao.updateUser(user)).thenReturn(ResponseCode.OK);

        ResponseCode actualCode = userService.updateUser(user);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void updateUser_failUpdate() {
        UserEntity user = new UserEntity();
        Mockito.when(mockUserDao.updateUser(user)).thenReturn(ResponseCode.ERROR);

        ResponseCode actualCode = userService.updateUser(user);

        assertEquals(ResponseCode.ERROR, actualCode);
    }

    @Test
    public void deleteUser_successDelete() {
        UserEntity user = new UserEntity();
        Mockito.when(mockUserDao.deleteUser(user)).thenReturn(ResponseCode.OK);

        ResponseCode actualCode = userService.deleteUser(user);

        assertEquals(ResponseCode.OK, actualCode);
    }

    @Test
    public void deleteUser_failDelete() {
        UserEntity user = new UserEntity();
        Mockito.when(mockUserDao.deleteUser(user)).thenReturn(ResponseCode.ERROR);

        ResponseCode actualCode = userService.deleteUser(user);

        assertEquals(ResponseCode.ERROR, actualCode);
    }
}
