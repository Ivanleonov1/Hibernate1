package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS users" +
            " (id mediumint not null auto_increment, name VARCHAR(50), " +
            "lastname VARCHAR(50), " +
            "age tinyint, " +
            "PRIMARY KEY (id))";
    private static final String DROP = "DROP TABLE IF EXISTS users";
    private static final String CLEAN = "TRUNCATE TABLE users";
    private static final String FROM_USER = "from User";
    private final SessionFactory sessionFactory = Util.getConnection();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {

        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(CREATE).executeUpdate();
            transaction.commit();
            session.close();
            System.out.println("Таблица создана");
        } catch (HibernateException e) {
            e.printStackTrace();

        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {

        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(DROP).executeUpdate();
            transaction.commit();
            session.close();
            System.out.println("Таблица удалена");
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = null;
        if (user != null) {

            Transaction transaction = null;
            Session session = null;
            try {
                session = sessionFactory.openSession();
                transaction = session.beginTransaction();
                session.save(new User(name, lastName, age));
                transaction.commit();
                session.close();
                System.out.println("User с именем – " + name + " добавлен в базу данных");
            } catch (HibernateException e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
            } finally {
                session.close();
            }
        }
    }

    @Override
    public void removeUserById(long id) {

        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
            session.close();
            System.out.println("User удален");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {

        Transaction transaction = null;
        Session session = null;
        List<User> userList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            Query query = session.createQuery(FROM_USER);
            transaction = session.beginTransaction();
            userList = query.list();
            session.close();
            transaction.commit();
            return userList;
        } catch (HibernateException e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.close();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {

        Transaction transaction = null;

        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.createNativeQuery(CLEAN).executeUpdate();
            transaction.commit();
            session.close();
            System.out.println("Таблица очищена");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }
}
