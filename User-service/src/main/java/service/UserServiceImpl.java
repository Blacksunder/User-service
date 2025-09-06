package service;

import entity.UserEntity;
import enums.ResponseCode;
import lombok.AllArgsConstructor;
import repository.UserDaoImpl;
import repository.UserDao;

import java.util.List;

@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    @Override
    public UserEntity getUserById(String uuid) {
        return userDao.getUserById(uuid);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public ResponseCode saveUser(UserEntity user) {
        return userDao.saveUser(user);
    }

    @Override
    public ResponseCode updateUser(UserEntity user) {
        return userDao.updateUser(user);
    }

    @Override
    public ResponseCode deleteUser(UserEntity user) {
        return userDao.deleteUser(user);
    }
}
