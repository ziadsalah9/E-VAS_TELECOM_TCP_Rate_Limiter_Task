package Repository;

import java.time.Instant;

import client.configs.RateLimitConfig;
import redis.clients.jedis.Jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class RedisRateLimitRepository {



    private static final Logger logger = LoggerFactory.getLogger(RedisRateLimitRepository.class);


    public long incrementAndGet(String client_id){

        long currentWindow= Instant.now().getEpochSecond()/ RateLimitConfig.WINDOW_SECONDS;  //ex: 1500 sec / 600 sec
        String key = "rate:" + client_id +":"+ currentWindow;
        try (Jedis jedis = RedisConnectionFactory.create()) {

        Long count = jedis.incr(key);
            if (count == 1) {
                jedis.expire(key,RateLimitConfig. WINDOW_SECONDS);
            }

            return count;


        }
    }


    public boolean allowRequestSliding(String clientId) {
        long now = Instant.now().toEpochMilli();
        String key = "sw:" + clientId;
        try (Jedis jedis = RedisConnectionFactory.create()) {

            jedis.zremrangeByScore(key, 0, now - (RateLimitConfig.WINDOW_SECONDS * 1000));

            long count = jedis.zcard(key); // no of requests
            if (count >= RateLimitConfig.LIMIT) {
                logger.warn("DENY request for client={} count={} timestamp={}", clientId, count, Instant.now());
                return false; // DENY

            }

            String uniqueValue = now + "-" + java.util.UUID.randomUUID();

            jedis.zadd(key, now, uniqueValue);
            jedis.expire(key, RateLimitConfig.WINDOW_SECONDS + 1);
            return true; // ALLOW

        } catch (Exception e) {
            logger.error("Redis unavailable, switching to FAIL OPEN mode", e);
            return true;
        }
    }

}
