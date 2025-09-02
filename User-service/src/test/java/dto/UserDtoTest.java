package dto;

import entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {
    @Test
    public void equals_shouldBeEquals() {
        UserDto dto1 = new UserDto("aaa", "bbb", 40);
        UserDto dto2 = new UserDto("aaa", "bbb", 40);

        assertEquals(dto1, dto2);
    }

    @Test
    public void equals_shouldBeNotEqualsDueToFields() {
        UserDto dto1 = new UserDto("AAA", "bbb", 40);
        UserDto dto2 = new UserDto("aaa", "bbb", 40);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void equals_shouldBeNotEqualsDueToClass() {
        UserDto dto1 = new UserDto("AAA", "bbb", 40);
        UserEntity entity = new UserEntity();

        assertNotEquals(dto1, entity);
    }

    @Test
    public void equals_shouldBeNotEqualsDueToNull() {
        UserDto dto1 = new UserDto("AAA", "bbb", 40);

        assertNotEquals(dto1, null);
    }
}
