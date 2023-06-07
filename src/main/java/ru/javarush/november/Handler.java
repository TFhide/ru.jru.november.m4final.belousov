package ru.javarush.november;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import ru.javarush.november.entity.*;
import ru.javarush.november.hibernate.MyRedisSession;
import ru.javarush.november.hibernate.MySessionFactory;
import ru.javarush.november.liquibase.Validate;
import ru.javarush.november.redis.CityCountry;
import ru.javarush.november.redis.Language;
import ru.javarush.november.repository.dao.CityDAO;
import ru.javarush.november.repository.dao.CountryDAO;
import ru.javarush.november.repository.dao.CountryLanguageDAO;
import ru.javarush.november.test.TestMysqlDB;
import ru.javarush.november.test.TestRedisDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class Handler {
    private CityDAO cityDAO;
    private CountryDAO countryDAO;
    private CountryLanguageDAO countryLanguageDAO;
    private MySessionFactory sessionFactory;
    private MyRedisSession redisSession;
    private RedisClient redisClient;
    private ObjectMapper mapper;
    private TestMysqlDB testMysqlDB;
    private TestRedisDB testRedisDB;

    public Handler()
    {
        sessionFactory = MySessionFactory.getInstance();
        redisSession = new MyRedisSession();
        redisClient = redisSession.prepareRedisClient();
        mapper = new ObjectMapper();
        this.cityDAO = new CityDAO(sessionFactory);
        this.countryDAO = new CountryDAO(sessionFactory);
        this.countryLanguageDAO = new CountryLanguageDAO(sessionFactory);
    }

    public List<City> fetchData()
    {
        try (Session session = sessionFactory.getSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();
            List<Country> countries = countryDAO.getAll();

            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getItems(i, step));
            }
            Hibernate.initialize(allCities);
            session.getTransaction().commit();
            return allCities;
        }
    }

    public List<CityCountry> transformData(List<City> cities)
    {
        return cities.stream().map(city -> {
            CityCountry res = new CityCountry();
            res.setId(city.getId());
            res.setName(city.getName());
            res.setPopulation(city.getPopulation());
            res.setDistrict(city.getDistrict());

            Country country = city.getCountry();
            res.setAlternativeCountryCode(country.getAlternativeCode());
            res.setContinent(country.getContinent());
            res.setCountryCode(country.getCode());
            res.setCountryName(country.getName());
            res.setCountryPopulation(country.getPopulation());
            res.setCountryRegion(country.getRegion());
            res.setCountrySurfaceArea(country.getSurfaceArea());

            Set<CountryLanguage> countryLanguages = country.getLanguages();
            Set<Language> languages = countryLanguages.stream().map(cl -> {
                Language language = new Language();
                language.setLanguage(cl.getLanguage());
                language.setPercentage(cl.getPercentage());
                language.setIsOfficial(cl.getIsOfficial());
                return language;
            }).collect(Collectors.toSet());
            res.setLanguages(languages);

            return res;
        }).collect(Collectors.toList());
    }

    public void pushToRedis(List<CityCountry> data)
    {
        try(StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                    try {
                        sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}