package backend;

import frontend.ModeConstants;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDao {

    public int saveUser(User user) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.isActive() || transaction.getRollbackOnly())) {
                transaction.rollback();
                return ModeConstants.ERROR;
            }
        }
        return ModeConstants.OK;
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

    public int updateUser(User user) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.isActive() || transaction.getRollbackOnly())) {
                transaction.rollback();
                return ModeConstants.ERROR;
            }
        }
        return ModeConstants.OK;
    }

    public int deleteUser(User user) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.isActive() || transaction.getRollbackOnly())) {
                transaction.rollback();
                return ModeConstants.ERROR;
            }
        }
        return ModeConstants.OK;
    }
}
