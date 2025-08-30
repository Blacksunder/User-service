package repository;

import entity.UserEntity;
import enums.InputMode;
import enums.ResponseCode;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDao implements UserDaoInterface {

    @Override
    public UserEntity getUserById(String uuid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(UserEntity.class, uuid);
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserEntity> query = session.createQuery("from UserEntity", UserEntity.class);
            return query.list();
        }
    }

    @Override
    public ResponseCode saveUser(UserEntity user) {
        return handleTransaction(user, InputMode.SAVE);
    }

    @Override
    public ResponseCode updateUser(UserEntity user) {
        return handleTransaction(user, InputMode.UPDATE);
    }

    @Override
    public ResponseCode deleteUser(UserEntity user) {
        return handleTransaction(user, InputMode.DELETE);
    }

    private ResponseCode handleTransaction(UserEntity user, InputMode mode) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            switch (mode) {
                case SAVE -> session.persist(user);
                case UPDATE -> session.merge(user);
                case DELETE -> session.remove(user);
                default -> {}
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && (transaction.isActive() || transaction.getRollbackOnly())) {
                transaction.rollback();
                return ResponseCode.ERROR;
            }
        }
        return ResponseCode.OK;
    }
}
