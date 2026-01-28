package client.configs;

public class RateLimitConfig {

    public static final int LIMIT = Integer.parseInt(System.getProperty("RATE_LIMIT", "10"));

    public static final int WINDOW_SECONDS =
            Integer.parseInt(System.getProperty("WINDOW_SECONDS", "60"));

    public static final int SERVER_PORT =
            Integer.parseInt(System.getProperty("SERVER_PORT", "9090"));
    public static final int SLIDING_WINDOW_SERVER_PORT =
            Integer.parseInt(System.getProperty("SERVER_PORT", "9091"));

    public static final int THREAD_POOL_SIZE =
            Integer.parseInt(System.getProperty("THREAD_POOL_SIZE", "20"));

    public static final int SLIDING_WINDOW_THREAD_POOL_SIZE =
            Integer.parseInt(System.getProperty("THREAD_POOL_SIZE", "10"));
}
