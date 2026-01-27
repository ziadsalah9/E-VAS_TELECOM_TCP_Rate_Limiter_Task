package Repository;

import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisRateLimitRepositoryMultiClientTest {

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
    void testMultipleClientsLimit() {
        String[] clients = {"clientA", "clientB", "clientC"};

        // كل client ينفذ 10 requests → ALLOW
        for (int i = 1; i <= 10; i++) {
            for (String clientId : clients) {
                long count = repository.incrementAndGet(clientId);
                assertTrue(count <= 10, clientId + " request " + i + " should be ALLOW");
            }
        }

        // Extra request لكل client → DENY
        for (String clientId : clients) {
            long count = repository.incrementAndGet(clientId);
            assertTrue(count > 10, clientId + " extra request should be DENY");
        }
    }

    @Test
    @Order(2)
    void testWindowResetForMultipleClients() throws InterruptedException {
        String[] clients = {"clientA", "clientB", "clientC"};

        // انتظر انتهاء ال window
        Thread.sleep(61000); // 61 ثانية لتأكيد reset

        // بعد reset → كل client request الأول → ALLOW
        for (String clientId : clients) {
            long count = repository.incrementAndGet(clientId);
            assertEquals(1, count, clientId + " should start at 1 after window reset");
        }
    }
}
