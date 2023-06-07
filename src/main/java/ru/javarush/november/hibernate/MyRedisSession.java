package ru.javarush.november.hibernate;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public class MyRedisSession {
    private RedisClient redisClient;

    public RedisClient prepareRedisClient()
    {
        redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        try(StatefulRedisConnection<String, String> connect = redisClient.connect()) {
            System.out.println("\nConnected to Redis\n");
        }
        return redisClient;
    }

    public void shutdownRedisSession()
    {
        if(redisClient.connect().isOpen()) {
            redisClient.shutdown();
        }
    }
}
