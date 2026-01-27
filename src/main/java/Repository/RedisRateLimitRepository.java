package Repository;

import java.time.Instant;

import redis.clients.jedis.Jedis;

public class RedisRateLimitRepository {


    private static  final int fixed_window_seconde= 60;

    public long incrementAndGet(String client_id){

        long currentWindow= Instant.now().getEpochSecond()/fixed_window_seconde;  //ex: 1500 sec / 600 sec
        String key = "rate:" + client_id +":"+ currentWindow;
        try (Jedis jedis = RedisConnectionFactory.create()) {

        Long count = jedis.incr(key);
            if (count == 1) {
                jedis.expire(key, fixed_window_seconde);
            }

            return count;


        }
    }
}
