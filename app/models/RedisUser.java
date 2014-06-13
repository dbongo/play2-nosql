package models;

import redis.clients.jedis.Jedis;

/**
 * User model for Redis
 *
 * @author vdubois
 */
public class RedisUser extends User {

    private static final String USER_KEY = "user:%s";
    private static final String EMAIL = "email";
    private static final String FULL_NAME = "fullName";
    private static final String USER_PASSWORD = "password";

    private static Jedis jedis() {
        return RedisHelper.JEDIS_INSTANCE;
    }

    public void save() {
        String userKey = String.format(USER_KEY, userId);
        jedis().hset(userKey, EMAIL, email);
        jedis().hset(userKey, FULL_NAME, fullName);
        jedis().hset(userKey, USER_PASSWORD, password);
    }

    public void remove() {
        String userKey = String.format(USER_KEY, userId);
        jedis().hdel(userKey, EMAIL);
        jedis().hdel(userKey, FULL_NAME);
        jedis().hdel(userKey, USER_PASSWORD);
    }

    public static User findByUserId(String userId) {
        String userKey = String.format(USER_KEY, userId);
        User user = null;
        if (exists(userId)) {
            user = new RedisUser();
            user.email = jedis().hget(userKey, EMAIL);
            user.fullName = jedis().hget(userKey, FULL_NAME);
            user.password = jedis().hget(userKey, USER_PASSWORD);
            user.userId = userId;
        }
        return user;
    }

    public static boolean exists(String userId) {
        String userKey = String.format(USER_KEY, userId);
        return jedis().hexists(userKey, EMAIL);
    }
}
