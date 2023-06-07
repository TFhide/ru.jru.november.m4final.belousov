package ru.javarush.november.repository.dao;

import org.hibernate.query.Query;
import ru.javarush.november.entity.City;
import ru.javarush.november.hibernate.MySessionFactory;
import ru.javarush.november.repository.GenericDAO;

public class CityDAO extends GenericDAO<City> {
    public CityDAO(MySessionFactory mySessionFactory) {
        super(City.class, mySessionFactory);
    }

    @Override
    public City getById(int id)
    {
        Query<City> query = getCurrentSession().createQuery("select c from City c join fetch c.country where c.id = :ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }
}
