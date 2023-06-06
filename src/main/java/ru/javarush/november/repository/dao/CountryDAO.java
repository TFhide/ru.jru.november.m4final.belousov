package ru.javarush.november.repository.dao;

import ru.javarush.november.entity.Country;
import ru.javarush.november.hibernate.MySessionFactory;
import ru.javarush.november.repository.GenericDAO;
import java.util.List;

public class CountryDAO extends GenericDAO<Country> {
    public CountryDAO(MySessionFactory mySessionFactory) {
        super(Country.class, mySessionFactory);
    }
    @Override
    public List<Country> getAll() {
            return getCurrentSession().createQuery("select  c from Country c join fetch c.languages").list();
    }
}
