package repository;

import entity.UserEntity;
import enums.ResponseCode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    protected static PostgreSQLContainer<?> postgreSQLContainer;
    protected static SessionFactory sessionFactory;
    private static UserDaoInterface userDao;

    private static final List<String> ids = List.of("100", "200", "300");
    private static final List<String> names = List.of("aaa", "bbb", "ccc");
    private static final List<String> emails = List.of("a", "b", "c");
    private static final List<Integer> ages = List.of(10, 20, 30);
    private static final LocalDateTime time = LocalDateTime.now();

    @BeforeAll
    public static void start() {
        postgreSQLContainer = new PostgreSQLContainer<>(
                "postgres:15-alpine")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        postgreSQLContainer.start();

        org.hibernate.cfg.Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgreSQLContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", postgreSQLContainer.getPassword());
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.addAnnotatedClass(UserEntity.class);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        sessionFactory = configuration.buildSessionFactory(registry);

        userDao = new UserDao(sessionFactory);
    }

    @AfterAll
    public static void stop() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }

    @BeforeEach
    public void prepareDb() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.createNativeMutationQuery("delete from users").executeUpdate();
            for (int i = 0; i < ids.size(); ++i) {
                UserEntity user = new UserEntity(ids.get(i), names.get(i),
                        emails.get(i), ages.get(i), time);
                session.persist(user);
            }
            session.getTransaction().commit();
        }
    }

    @Test
    public void getUserById_getExistingUser() {
        UserEntity expected = new UserEntity(ids.get(0), names.get(0), emails.get(0), ages.get(0), time);

        UserEntity actual = userDao.getUserById(ids.get(0));

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
        for (int i = 0; i < ids.size(); ++i) {
            expected.add(new UserEntity(ids.get(i), names.get(i), emails.get(i), ages.get(i), time));
        }

        List<UserEntity> actual = userDao.getAllUsers();

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertTrue(compareUserEntityWithProxy(actual.get(i), expected.get(i)));
        }
    }

    @Test
    public void saveUser_successfulSaving() {
        UserEntity entity = new UserEntity("a", "b", "c", 5, time);
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
        UserEntity entity = new UserEntity(ids.get(0), "b", "c", 5, time);
        ResponseCode expected = ResponseCode.ERROR;

        ResponseCode actual = userDao.saveUser(entity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, ids.get(0));
        }

        assertEquals(expected, actual);
        assertFalse(compareUserEntityWithProxy(dbEntity, entity));
    }

    @Test
    public void updateUser_successfulUpdating() {
        UserEntity entity = new UserEntity(ids.get(0), names.get(1), emails.get(1), ages.get(1), time);
        ResponseCode expectedCode = ResponseCode.OK;

        ResponseCode actualCode = userDao.updateUser(entity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, ids.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertTrue(compareUserEntityWithProxy(dbEntity, entity));
    }

    @Test
    public void updateUser_failUpdating() {
        UserEntity unexistedEntity = new UserEntity("a", "b", "c", 0, time);
        UserEntity expectedEntity = new UserEntity(ids.get(0), names.get(0), emails.get(0), ages.get(0), time);
        ResponseCode expectedCode = ResponseCode.ERROR;

        ResponseCode actualCode = userDao.updateUser(unexistedEntity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, ids.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertTrue(compareUserEntityWithProxy(dbEntity, expectedEntity));
    }

    @Test
    public void deleteUser_successfulDelete() {
        UserEntity existingEntity = new UserEntity(ids.get(0), names.get(0), emails.get(0), ages.get(0), time);
        ResponseCode expectedCode = ResponseCode.OK;

        ResponseCode actualCode = userDao.deleteUser(existingEntity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, ids.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertNull(dbEntity);
    }

    @Test
    public void deleteUser_failDelete() {
        UserEntity unexistingEntity = new UserEntity("aaaaa", "fgdfg", "dgdfg", 0, time);
        ResponseCode expectedCode = ResponseCode.ERROR;

        ResponseCode actualCode = userDao.deleteUser(unexistingEntity);
        UserEntity dbEntity;
        try (Session session = sessionFactory.openSession()) {
            dbEntity = session.get(UserEntity.class, ids.get(0));
        }

        assertEquals(expectedCode, actualCode);
        assertNotNull(dbEntity);
    }

    private static boolean compareUserEntityWithProxy(UserEntity actual, UserEntity expected) {
        return actual.getUuid().equals(expected.getUuid()) && actual.getName().equals(expected.getName()) &&
                actual.getEmail().equals(expected.getEmail()) && actual.getAge() == expected.getAge() &&
                expected.getCreatedAt().compareTo(actual.getCreatedAt()) < 1000;
    }
}
