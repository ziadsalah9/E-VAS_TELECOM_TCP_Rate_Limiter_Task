package service;

import Repository.RedisRateLimitRepository;
import configs.RateLimitConfig;
import domain.RateLimitResult;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RateLimitService {

    // Rule: 10 req/min
    private static final Logger logger = LoggerFactory.getLogger(RedisRateLimitRepository.class);

//    private static final int limit = 10 ;

    private final RedisRateLimitRepository _RdlRepository;


    public RateLimitService(RedisRateLimitRepository RdlRepository) {
        _RdlRepository = RdlRepository;
    }

    public RateLimitResult checkRateLimitResult(String clientId) {

        var currentCount = _RdlRepository.incrementAndGet(clientId);

//        if(currentCount <= limit){
//            return RateLimitResult.ALLOW;
//        }
//        return RateLimitResult.DENY;
//


        if (currentCount > RateLimitConfig.LIMIT){
            logger.warn("DENY request for client={} count={} timestamp={}", clientId, currentCount, Instant.now());

            return RateLimitResult.DENY;
        }
        return RateLimitResult.ALLOW;
    }
}
