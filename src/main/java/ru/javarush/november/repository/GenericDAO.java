package ru.javarush.november.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.javarush.november.hibernate.MySessionFactory;

import java.util.List;

public abstract class GenericDAO<T> {

    Class<T> aClass;
    MySessionFactory sessionFactory;

    public GenericDAO(Class<T> aClass, MySessionFactory sessionFactory) {
        this.aClass = aClass;
        this.sessionFactory = sessionFactory;
    }

    public T getById(final int id)
    {
        return (T) getCurrentSession().get(aClass, id);
    }

    public List<T> getItems(int offset, int count)
    {
            Query<T> query = getCurrentSession().createQuery("from " + aClass.getName(), aClass);
            query.setFirstResult(offset);
            query.setMaxResults(count);
            return query.list();
    }

    public List<T> getAll()
    {
        List<T> list = getCurrentSession().createQuery("from " + aClass.getName(), aClass).list();
        return list;
    }

    public int getTotalCount()
    {
        Query<Long> query = getCurrentSession().createQuery("select count(*) from " + aClass.getName(), Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    public T save (T entity)
    {
        return entity;
    }

    public T update (T entity)
    {
        return entity;
    }

    public T delete (T entity)
    {
       return entity;
    }

    public Session getCurrentSession()
    {
        return sessionFactory.getSession();
    }

}
