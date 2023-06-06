package ru.javarush.november.repository.dao;

import ru.javarush.november.entity.City;
import ru.javarush.november.hibernate.MySessionFactory;
import ru.javarush.november.repository.GenericDAO;

public class CityDAO extends GenericDAO<City> {
    public CityDAO(MySessionFactory mySessionFactory) {
        super(City.class, mySessionFactory);
    }

}
