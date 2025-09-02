package repository;

import entity.UserEntity;
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
import org.testng.AssertJUnit;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    }

    @Test
    public void saveUser() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUser() {
    }

    private static boolean compareUserEntityWithProxy(UserEntity actual, UserEntity expected) {
        return actual.getUuid().equals(expected.getUuid()) && actual.getName().equals(expected.getName()) &&
                actual.getEmail().equals(expected.getEmail()) && actual.getAge() == expected.getAge() &&
                expected.getCreatedAt().compareTo(actual.getCreatedAt()) < 1000;
    }
}
