package ru.javarush.november.repository.dao;

import ru.javarush.november.entity.CountryLanguage;
import ru.javarush.november.hibernate.MySessionFactory;
import ru.javarush.november.repository.GenericDAO;

public class CountryLanguageDAO extends GenericDAO<CountryLanguage> {
    public CountryLanguageDAO(MySessionFactory mySessionFactory) {
        super(CountryLanguage.class, mySessionFactory);
    }
}
