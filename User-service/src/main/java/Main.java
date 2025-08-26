import backend.HibernateUtil;
import backend.User;
import backend.UserDao;
import jakarta.transaction.SystemException;

public class Main {
    public static void main(String[] args) throws SystemException {
        UserDao userDao = new UserDao();

        //userDao.saveUser(new User("Vlad", "agaev.vlad@bk.ru", 24));
        userDao.getAllUsers().forEach(System.out::println);

        HibernateUtil.shutDown();
    }
}
