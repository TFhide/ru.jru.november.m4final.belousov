package ru.javarush.november;

import ru.javarush.november.entity.City;
import ru.javarush.november.liquibase.Validate;
import ru.javarush.november.redis.CityCountry;
import ru.javarush.november.test.TestMysqlDB;
import ru.javarush.november.test.TestRedisDB;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class InitApp {
    private static Handler handler;

    static
    {
        handler = new Handler();
    }

    public static void main(String[] args) throws Exception
    {
        Validate validate = new Validate();
        validate.initLiquibase();
        List<City> allCities = handler.fetchData();
        List<CityCountry> preparedData = handler.transformData(allCities);
        handler.pushToRedis(preparedData);
        handler.getRedisSession().shutdownRedisSession();
        handler.getSessionFactory().shutdownSession();
        getDataLoadingTime();
    }

    public static void getDataLoadingTime()
    {
        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        Instant startRedis = Instant.now();
        new TestRedisDB().testRedisData(ids);
        Instant stopRedis = Instant.now();

        Instant startMysql = Instant.now();
        new TestMysqlDB().testMysqlData(ids);
        Instant stopMysql = Instant.now();

        System.out.printf("%s:\t%d ms and %d ns\n", "Redis",
                Duration.between(startRedis, stopRedis).toMillis(), Duration.between(startRedis, stopRedis).toNanos());
        System.out.printf("%s:\t%d ms and %d ns\n", "Mysql",
                Duration.between(startMysql, stopMysql).toMillis(), Duration.between(startMysql, stopMysql).toNanos());
    }
}
