package service;

import entity.UserEntity;
import enums.ResponseCode;
import lombok.Getter;
import lombok.Setter;
import repository.UserDao;
import repository.UserDaoInterface;

import java.util.List;

@Getter
@Setter
public class UserService implements UserServiceInterface {
    private final UserDaoInterface userDao = new UserDao();

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
