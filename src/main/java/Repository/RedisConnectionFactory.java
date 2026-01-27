package Repository;

import redis.clients.jedis.Jedis;

public class RedisConnectionFactory {
        private static final String HOST = "localhost";
        private static final int PORT = 6379;

        public static Jedis create() {
            return new Jedis(HOST, PORT);

}
