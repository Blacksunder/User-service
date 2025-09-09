package repository;

import entity.UserEntity;
import enums.ResponseCode;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static repository.TestConstants.*;
import static repository.HibernateConfigurer.sessionFactory;

class UserRepositoryImplTest {
    private static final HibernateConfigurer hibernateConfigurer = new HibernateConfigurer();
    private static UserDao userDao;

    @BeforeAll
    public static void start() {
        hibernateConfigurer.buildSession();
        userDao = new UserDaoImpl(sessionFactory);
    }

    @AfterAll
    public static void stop() {
        hibernateConfigurer.stopSession();
    }

    @BeforeEach
    public void prepareDb() {
        hibernateConfigurer.prepareDb();
    }

    @Test
    public void getUserById_getExistingUser() {
        UserEntity expected = new UserEntity(IDS.get(0), NAMES.get(0), EMAILS.get(0), AGES.get(0), TIME);

        UserEntity actual = userDao.getUserById(IDS.get(0));

        assertTrue(compareUserEntityWithProxy(actual, expected));
    }

    @Test
    public void getUserById_getUnexistingUser() {
        UserEntity actual = userDao.getUserById("zffggdfgdfg");

        assertNull(actual);
    }

    @Test
    public void getAllUsers() {
        List<UserEntity> expected = new ArrayList<>();
        for (int i = 0; i < IDS.size(); ++i) {
            expected.add(new UserEntity(IDS.get(i), NAMES.get(i), EMAILS.get(i), AGES.get(i), TIME));
        }

        List<UserEntity> actual = userDao.getAllUsers();

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertTrue(compareUserEntityWithProxy(actual.get(i), expected.get(i)));
        }
    }

    @Test
    public void saveUser_successfulSaving() {
        UserEntity entity = new UserEntity("a", "b", "c", 5, TIME);
        ResponseCode expected = ResponseCode.OK;

        ResponseCode actual = userDao.saveUser(entity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, "a");
        }

        assertEquals(expected, actual);
        assertTrue(compareUserEntityWithProxy(dbEntity, entity));
    }

    @Test
    public void saveUser_failSaving() {
        UserEntity entity = new UserEntity(IDS.get(0), "b", "c", 5, TIME);
        ResponseCode expected = ResponseCode.ERROR;

        ResponseCode actual = userDao.saveUser(entity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, IDS.get(0));
        }

        assertEquals(expected, actual);
        assertFalse(compareUserEntityWithProxy(dbEntity, entity));
    }

    @Test
    public void updateUser_successfulUpdating() {
        UserEntity entity = new UserEntity(IDS.get(0), NAMES.get(1), EMAILS.get(1), AGES.get(1), TIME);
        ResponseCode expectedCode = ResponseCode.OK;

        ResponseCode actualCode = userDao.updateUser(entity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, IDS.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertTrue(compareUserEntityWithProxy(dbEntity, entity));
    }

    @Test
    public void updateUser_failUpdating() {
        UserEntity unexistedEntity = new UserEntity("a", "b", "c", 0, TIME);
        UserEntity expectedEntity = new UserEntity(IDS.get(0), NAMES.get(0), EMAILS.get(0), AGES.get(0), TIME);
        ResponseCode expectedCode = ResponseCode.ERROR;

        ResponseCode actualCode = userDao.updateUser(unexistedEntity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, IDS.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertTrue(compareUserEntityWithProxy(dbEntity, expectedEntity));
    }

    @Test
    public void deleteUser_successfulDelete() {
        UserEntity existingEntity = new UserEntity(IDS.get(0), NAMES.get(0), EMAILS.get(0), AGES.get(0), TIME);
        ResponseCode expectedCode = ResponseCode.OK;

        ResponseCode actualCode = userDao.deleteUser(existingEntity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, IDS.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertNull(dbEntity);
    }

    @Test
    public void deleteUser_failDelete() {
        UserEntity unexistingEntity = new UserEntity("aaaaa", "fgdfg", "dgdfg", 0, TIME);
        ResponseCode expectedCode = ResponseCode.ERROR;

        ResponseCode actualCode = userDao.deleteUser(unexistingEntity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, IDS.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertNotNull(dbEntity);
    }

    private static boolean compareUserEntityWithProxy(UserEntity actual, UserEntity expected) {
        return actual.getUuid().equals(expected.getUuid()) &&
                actual.getName().equals(expected.getName()) &&
                actual.getEmail().equals(expected.getEmail()) &&
                actual.getAge() == expected.getAge() &&
                expected.getCreatedAt().compareTo(actual.getCreatedAt()) < 1000;
    }
}
