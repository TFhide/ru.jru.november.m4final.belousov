package ru.javarush.november.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.javarush.november.entity.City;
import ru.javarush.november.entity.Country;
import ru.javarush.november.entity.CountryLanguage;
import static java.util.Objects.isNull;

public class MySessionFactory {
    private static MySessionFactory instance;
    private SessionFactory sessionFactory;

    private MySessionFactory()
    {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(CountryLanguage.class)
                .buildSessionFactory();
    }

    public static MySessionFactory getInstance()
    {
        if (isNull(instance)) {
            synchronized (MySessionFactory.class) {
                if(isNull(instance)) {
                    instance = new MySessionFactory();
                }
            }
        }
        return instance;
    }

    public Session getSession()
    {
            return sessionFactory.openSession();
    }
}
