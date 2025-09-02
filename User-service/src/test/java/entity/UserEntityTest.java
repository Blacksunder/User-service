package entity;

import dto.UserDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {
    @Test
    public void equals_shouldReturnTrue() {
        LocalDateTime time = LocalDateTime.now();
        UserEntity entity1 = new UserEntity("12345", "aaa", "bbb", 20, time);
        UserEntity entity2 = new UserEntity("12345", "aaa", "bbb", 20, time);

        assertEquals(entity1, entity2);
    }

    @Test
    public void equals_shouldReturnFalseDueToFields() {
        LocalDateTime time = LocalDateTime.now();
        UserEntity entity1 = new UserEntity("12345", "aaa", "bbb", 20, time);
        UserEntity entity2 = new UserEntity("12345", "aaa", "bbb", 100, time);

        assertNotEquals(entity1, entity2);
    }

    @Test
    public void equals_shouldReturnFalseDueToClass() {
        LocalDateTime time = LocalDateTime.now();
        UserEntity entity1 = new UserEntity("12345", "aaa", "bbb", 20, time);
        UserDto dto = new UserDto();

        assertNotEquals(entity1, dto);
    }

    @Test
    public void equals_shouldReturnFalseDueToNull() {
        LocalDateTime time = LocalDateTime.now();
        UserEntity entity1 = new UserEntity("12345", "aaa", "bbb", 20, time);

        assertNotEquals(entity1, null);
    }

    @Test
    public void parametrizedConstructor_timeAndUuidMustBeCorrect() {
        UserEntity entity = new UserEntity("aaa", "bbb", 20);

        assertEquals(entity.getUuid().length(), UUID.randomUUID().toString().length());
        assertTrue(entity.getCreatedAt().isBefore(LocalDateTime.now()));
    }
}
