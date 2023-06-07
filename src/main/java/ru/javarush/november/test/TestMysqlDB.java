package ru.javarush.november.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.javarush.november.Handler;
import ru.javarush.november.entity.City;
import ru.javarush.november.entity.CountryLanguage;
import ru.javarush.november.repository.dao.CityDAO;

import java.util.List;
import java.util.Set;

public class TestMysqlDB {

    private Handler handler;
    private CityDAO cityDAO;

    public TestMysqlDB()
    {
        handler = new Handler();
        cityDAO = new CityDAO(handler.getSessionFactory());
    }

    public void testMysqlData(List<Integer> ids)
    {
        try(Session session = handler.getSessionFactory().getSession()) {
            Transaction tx = session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDAO.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            tx.commit();
        }
    }
}
