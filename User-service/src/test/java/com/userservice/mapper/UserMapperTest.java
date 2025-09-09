package mapper;

import dto.UserDto;
import entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    @Test
    public void userEntityToDto_compareWithCorrectDto() {
        UserEntity entity = new UserEntity("aaa", "bbb", 30);
        UserMapper mapper = new UserMapper();
        UserDto expected = new UserDto("aaa", "bbb", 30);

        UserDto actual = mapper.userEntityToDto(entity);

        assertEquals(actual, expected);
    }

    @Test
    public void userEntityToDto_compareWithIncorrectDto() {
        UserEntity entity = new UserEntity("aaa", "bbb", 30);
        UserMapper mapper = new UserMapper();
        UserDto expected = new UserDto("", "bbb", 30);

        UserDto actual = mapper.userEntityToDto(entity);

        assertNotEquals(actual, expected);
    }

    @Test
    public void userEntityToDto_mappingNull() {
        UserMapper mapper = new UserMapper();

        assertNull(mapper.userEntityToDto(null));
    }
}
