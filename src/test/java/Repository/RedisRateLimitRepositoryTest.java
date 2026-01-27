package Repository;

import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisRateLimitRepositoryTest {

    private static RedisRateLimitRepository repository;
    private static Jedis jedis;

    @BeforeAll
    static void setup() {
        repository = new RedisRateLimitRepository();
        jedis = RedisConnectionFactory.create();
        jedis.flushDB(); // تنظيف Redis قبل الاختبارات
    }

    @AfterAll
    static void cleanup() {
        if (jedis != null) jedis.close();
    }

    @Test
    @Order(1)
    void testIncrementAndLimit() throws InterruptedException {
        String clientId = "testClient";

        // ALLOW 10 requests
        for (int i = 1; i <= 10; i++) {
            long count = repository.incrementAndGet(clientId);
            assertTrue(count <= 10, "Request " + i + " should be ALLOW");
        }

        // Request 11 → DENY
        long count11 = repository.incrementAndGet(clientId);
        assertTrue(count11 > 10, "11th request should be DENY");

        // انتظر حتى تنتهي الـ window
        Thread.sleep(61000); // 61 ثانية لتأكد reset

        // Request بعد expiry → ALLOW
        long countAfterExpire = repository.incrementAndGet(clientId);
        assertEquals(1, countAfterExpire, "After window reset, counter should start at 1");
    }
}
