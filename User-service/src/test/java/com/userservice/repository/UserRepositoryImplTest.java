package com.userservice.repository;

import com.userservice.entity.UserEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.userservice.repository.TestConstants.*;

@DataJpaTest
class UserRepositoryImplTest {

    @LocalServerPort
    private String port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void prepareDb() {
        userRepository.deleteAll();
        for (int i = 0; i < IDS.size(); ++i) {
            UserEntity user = new UserEntity(IDS.get(i), NAMES.get(i),
                    EMAILS.get(i), AGES.get(i), TIME);
            userRepository.save(user);
        }
    }

    @Test
    public void getUserById_getExistingUser() {
        UserEntity expected = new UserEntity(IDS.get(0), NAMES.get(0), EMAILS.get(0), AGES.get(0), TIME);

        UserEntity actual = userRepository.findById(IDS.get(0)).orElse(null);

        assertTrue(compareUserEntityWithProxy(expected, actual));
    }

    @Test
    public void getUserById_getUnexistingUser() {
        UserEntity actual = userRepository.findById("zffggdfgdfg").orElse(null);

        assertNull(actual);
    }

    @Test
    public void getAllUsers() {
        List<UserEntity> expected = new ArrayList<>();
        for (int i = 0; i < IDS.size(); ++i) {
            expected.add(new UserEntity(IDS.get(i), NAMES.get(i), EMAILS.get(i), AGES.get(i), TIME));
        }

        List<UserEntity> actual = (List<UserEntity>) userRepository.findAll();

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertTrue(compareUserEntityWithProxy(actual.get(i), expected.get(i)));
        }
    }

    @Test
    public void saveUser_successfulSaving() {
        UserEntity entity = new UserEntity("a", "b", "c", 5, TIME);

        UserEntity saved = userRepository.save(entity);

        assertTrue(compareUserEntityWithProxy(entity, saved));
    }

    @Test
    public void updateUser_successfulUpdating() {
        UserEntity entity = new UserEntity(IDS.get(0), NAMES.get(1), EMAILS.get(1), AGES.get(1), TIME);

        UserEntity updated = userRepository.save(entity);

        assertTrue(compareUserEntityWithProxy(updated, entity));
    }

    @Test
    public void deleteUser_successfulDelete() {
        UserEntity existingEntity = new UserEntity(IDS.get(0), NAMES.get(0), EMAILS.get(0), AGES.get(0), TIME);

        userRepository.deleteById(existingEntity.getUuid());
        UserEntity dbEntity = userRepository.findById(existingEntity.getUuid()).orElse(null);

        assertNull(dbEntity);
    }

    private static boolean compareUserEntityWithProxy(UserEntity actual, UserEntity expected) {
        return actual.getUuid().equals(expected.getUuid()) &&
                actual.getName().equals(expected.getName()) &&
                actual.getEmail().equals(expected.getEmail()) &&
                actual.getAge() == expected.getAge() &&
                expected.getCreatedAt().compareTo(actual.getCreatedAt()) < 1000;
    }
}
