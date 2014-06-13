package models;

import com.typesafe.plugin.RedisPlugin;
import play.Play;
import redis.clients.jedis.Jedis;

/**
 * TODO VDU - Mettre un commentaire
 *
 * @author SQLI
 */
public class RedisHelper {
    public static final Jedis JEDIS_INSTANCE = Play.application().plugin(RedisPlugin.class).jedisPool().getResource();
}
