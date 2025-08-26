package backend;

import jakarta.transaction.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.logging.Logger;

public class UserDao {

    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public void saveUser(User user) throws SystemException {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("User\n" + user + "\nis added");
        } catch (Exception e) {
            if (transaction != null) {
                logger.info("User\n" + user + "\nis not added");
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User getUserById(String uuid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, uuid);
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    public void updateUser(User user) throws SystemException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = (Transaction) session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) throws SystemException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = (Transaction) session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
