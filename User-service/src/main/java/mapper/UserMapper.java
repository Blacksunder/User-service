package mapper;

import dto.UserDto;
import entity.UserEntity;

public class UserMapper {
    public static UserDto UserEntityToDto(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getUuid(), user.getName(),
                user.getEmail(), user.getAge(), user.getCreatedAt());
    }
}
