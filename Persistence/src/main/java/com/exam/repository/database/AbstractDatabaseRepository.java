package com.exam.repository.database;

import com.exam.domain.Entity;
import com.exam.repository.CRUDRepository;
import com.exam.repository.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public abstract class AbstractDatabaseRepository<E extends Entity<ID>, ID extends Serializable> implements CRUDRepository<E, ID> {
    protected static final Logger logger = LogManager.getLogger();
    protected static SessionFactory sessionFactory;
    protected final Class<E> entityType;

    AbstractDatabaseRepository(Class<E> entityType) {
        this.entityType = entityType;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    public E findByID(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id is null");
        logger.info("Find " + entityType.getName() + " with id: " + id);

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query<E> query = session.createQuery("from " + this.entityType.getSimpleName() + " where id = :id", entityType);
                query.setParameter("id", id);
                E result = query.getSingleResult();
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
    public Iterable<E> findAll() {
        logger.info("Find all " + entityType.getName());
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<E> entities = session.createQuery("from " + this.entityType.getSimpleName(), entityType).list();
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

    @Override
    public E save(E entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("entity is null");
        logger.info("Save new " + entityType.getName());
        if (entity.getId() != null && this.findByID(entity.getId()) != null)
            throw new ValidationException("duplicate");

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                return null;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return entity;
    }

    @Override
    public E delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id is null");
        logger.info("Delete " + entityType.getName() + " with id: " + id);
        E entity = findByID(id);
        if (entity != null) {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.delete(entity);
                    tx.commit();
                    return entity;
                } catch (RuntimeException e) {
                    if (tx != null)
                        tx.rollback();
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity is null");
        logger.info("Update " + entityType.getName() + " with id: " + entity.getId());

        E old = this.findByID(entity.getId());
        if (old != null) {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.saveOrUpdate(entity);
                    tx.commit();
                    return old;
                } catch (RuntimeException e) {
                    if (tx != null)
                        tx.rollback();
                    return null;
                }
            }
        }
        return null;
    }
}
