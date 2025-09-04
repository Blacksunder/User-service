package service;

import entity.UserEntity;
import enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import repository.UserDao;
import repository.UserDaoInterface;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UserService implements UserServiceInterface {
    private UserDaoInterface userDao = new UserDao();

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
