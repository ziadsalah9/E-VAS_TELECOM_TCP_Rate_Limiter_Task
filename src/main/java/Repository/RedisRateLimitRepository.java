package Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import redis.clients.jedis.Jedis;

public class RedisRateLimitRepository {

   // before redis
//    private final ConcurrentHashMap<String, AtomicLong> store =
//            new ConcurrentHashMap<>();
//
//    public long incrementAndGet(String clientId) {
//
//        store.putIfAbsent(clientId, new AtomicLong(0));
//
//        return store.get(clientId).incrementAndGet();
//    }

    private static final String REDIS_HOST = "redis";
    private static final int REDIS_PORT = 6379;

    public static Jedis create() {
        return new Jedis(REDIS_HOST, REDIS_PORT);
    }



}
