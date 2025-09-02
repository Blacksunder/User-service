package mapper;

import dto.UserDto;
import entity.UserEntity;

public class UserMapper {
    public UserDto userEntityToDto(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getName(), user.getEmail(), user.getAge());
    }
}
