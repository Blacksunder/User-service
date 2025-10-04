package com.userservice.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {
    @Test
    public void parametrizedConstructor_timeAndUuidMustBeCorrect() {
        UserEntity entity = new UserEntity("aaa", "bbb", 20);
        LocalDateTime time = LocalDateTime.now().plusSeconds(1);

        assertEquals(entity.getUuid().length(), UUID.randomUUID().toString().length());
        assertTrue(entity.getCreatedAt().isBefore(time));
    }
}
