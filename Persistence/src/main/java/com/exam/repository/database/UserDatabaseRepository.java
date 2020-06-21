package com.exam.repository.database;

import com.exam.domain.User;
import com.exam.domain.UserType;
import com.exam.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDatabaseRepository extends AbstractDatabaseRepository<User, Integer> implements UserRepository {
    public UserDatabaseRepository() {
        super(User.class);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        if (username == null)
            throw new IllegalArgumentException("username is null");
        logger.info("Find " + entityType.getName() + " with username: " + username + " and password: " + password);

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query<User> query = session.createQuery("from " + this.entityType.getSimpleName() + " where username = :username and password = :password", entityType);
                query.setParameter("username", username);
                query.setParameter("password", password);
                User result = query.getSingleResult();
                tx.commit();
                return result;
            } catch (RuntimeException e) {
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public List<User> findAllByUserType(UserType userType) {
        logger.info("Find all " + entityType.getName() + " by user type");
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query<User> query = session.createQuery("from " + this.entityType.getSimpleName() + " where userType = :userType", entityType);
                query.setParameter("userType", userType);
                List<User> entities = query.list();
                tx.commit();
                return entities;
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }
}
