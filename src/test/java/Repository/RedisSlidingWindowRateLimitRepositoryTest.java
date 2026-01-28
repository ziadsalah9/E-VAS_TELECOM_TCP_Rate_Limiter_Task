package Repository;

import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.*;

public class RedisSlidingWindowRateLimitRepositoryTest {

    private RedisRateLimitRepository repository;
    private Jedis jedis;

    @BeforeEach
    void setup() {
        repository = new RedisRateLimitRepository();
        jedis = RedisConnectionFactory.create();
        jedis.flushDB(); // Clean redis before every test
    }

    @AfterEach
    void cleanup() {
        jedis.close();
    }

    @Test
    void shouldAllowFirst10Requests() {

        String clientId = "sliding-client-allow";

        for (int i = 1; i <= 10; i++) {
            boolean allowed = repository.allowRequestSliding(clientId);
            assertTrue(allowed, "Request " + i + " should be ALLOWED");
        }
    }

    @Test
    void shouldDenyAfterLimitReached() {

        String clientId = "sliding-client-deny";

        // Send LIMIT requests inside same window
        for (int i = 1; i <= 10; i++) {
            boolean allowed = repository.allowRequestSliding(clientId);
            assertTrue(allowed);
        }

        // Send overflow request
        boolean denied = repository.allowRequestSliding(clientId);

        assertFalse(denied, "Request after limit should be DENIED");
    }

    @Test
    void shouldAllowAfterWindowPass() throws InterruptedException {

        String clientId = "sliding-client-reset";

        for (int i = 1; i <= 10; i++) {
            repository.allowRequestSliding(clientId);
        }

        // Wait window expiry
        Thread.sleep(61000);

        boolean allowed = repository.allowRequestSliding(clientId);

        assertTrue(allowed, "Request after window reset should be ALLOWED");
    }

    @Test
    void shouldIsolateMultipleClients() {

        String client1 = "sliding-client-1";
        String client2 = "sliding-client-2";

        for (int i = 1; i <= 10; i++) {
            assertTrue(repository.allowRequestSliding(client1));
            assertTrue(repository.allowRequestSliding(client2));
        }

        assertFalse(repository.allowRequestSliding(client1));
        assertFalse(repository.allowRequestSliding(client2));
    }
}
