package service;

import Repository.RedisRateLimitRepository;
import domain.RateLimitResult;

public class RateLimitService {

    // Rule: 10 req/min

    private static final int limit = 10 ;

    private final RedisRateLimitRepository _RdlRepository;


    public RateLimitService(RedisRateLimitRepository RdlRepository) {
        _RdlRepository = RdlRepository;
    }

    public RateLimitResult checkRateLimitResult(String clientId) {

        var currentCount = _RdlRepository.incrementAndGet(clientId);

        if(currentCount <= limit){
            return RateLimitResult.ALLOW;
        }
        return RateLimitResult.DENY;
    }
}
