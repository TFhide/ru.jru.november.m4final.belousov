package ru.javarush.november.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import ru.javarush.november.Handler;
import ru.javarush.november.redis.CityCountry;

import java.util.List;

public class TestRedisDB {
    private Handler handler;

    public TestRedisDB()
    {
        handler = new Handler();
    }

    public void testRedisData(List<Integer> ids)
    {
        try(StatefulRedisConnection<String, String> connection = handler.getRedisClient().connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                    try {
                        handler.getMapper().readValue(value, CityCountry.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
