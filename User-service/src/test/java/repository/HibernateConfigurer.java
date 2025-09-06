package repository;

import entity.UserEntity;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import static repository.TestConstants.*;

@Getter
public class HibernateConfigurer {
    protected static PostgreSQLContainer<?> postgreSQLContainer;
    public static SessionFactory sessionFactory;


    public void buildSession() {
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
    }

    public void stopSession() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }

    public void prepareDb() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.createNativeMutationQuery("delete from users").executeUpdate();
            for (int i = 0; i < IDS.size(); ++i) {
                UserEntity user = new UserEntity(IDS.get(i), NAMES.get(i),
                        EMAILS.get(i), AGES.get(i), TIME);
                session.persist(user);
            }
            session.getTransaction().commit();
        }
    }
}
