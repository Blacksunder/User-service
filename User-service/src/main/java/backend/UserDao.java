package backend;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDao {

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

    public int saveUser(User user) {
        return handleTransaction(user, ModeConstants.SAVE);
    }

    public int updateUser(User user) {
        return handleTransaction(user, ModeConstants.UPDATE);
    }

    public int deleteUser(User user) {
        return handleTransaction(user, ModeConstants.DELETE);
    }

    private int handleTransaction(User user, int mode) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            switch (mode) {
                case ModeConstants.SAVE -> session.persist(user);
                case ModeConstants.UPDATE -> session.merge(user);
                case ModeConstants.DELETE -> session.remove(user);
                default -> {}
            }
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
