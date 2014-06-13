package models;

import redis.clients.jedis.Jedis;

/**
 * Token model for Redis
 *
 * @author SQLI
 */
public class RedisToken extends Token {

    private static Jedis jedis() {
        return RedisHelper.JEDIS_INSTANCE;
    }

    public void save() {
        jedis().set(token, userId);
        jedis().expire(token, 24 * 60 * 60);
    }

    public void remove() {
        jedis().del(token);
    }

    public static Token findByToken(String token) {
        Token redisToken = null;
        if (exists(token)) {
            redisToken = new RedisToken();
            redisToken.token = token;
            redisToken.userId = jedis().get(token);
        }
        return redisToken;
    }

    public static boolean exists(String token) {
        return jedis().exists(token);
    }
}
