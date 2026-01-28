# TCP Rate Limiter with Java & Redis


# OverView

This project implements a rate-limiting TCP server using **Java sockets** and **Redis**.  
It supports multiple concurrent clients,  and configurable rate limits, including **Fixed Window** and **Sliding Window** algorithms.  

---

## Features

- Raw TCP server (no Spring Boot, no HTTP/REST)
- Rate limiting per client using Redis
- Fixed Window and Sliding Window strategies
- Thread-safe & low-latency
- Configurable limits and window size
- Logging for denied requests
- Unit tests for repository and multi-client scenarios

## Dependencies

- Java 24+
- Maven
- Redis (Docker)
- Jedis 5.1.0 
- JUnit 5 (for unit tests)

# Setup & Installation
1. Run Redis using Docker Compose

# create docker-compose.yml
version: "3.9"
services:
  redis:
    image: redis:latest
    container_name: redis
    ports:
      - "6379:6379"

Command : docker-compose up -d
Redis will be available at localhost:6379.

# Configuration

You can configure rate limits, window size, ports, and thread pool size via system properties or directly in RateLimitConfig.java.

public static final int LIMIT = Integer.parseInt(System.getProperty("RATE_LIMIT", "10"));
public static final int WINDOW_SECONDS = Integer.parseInt(System.getProperty("WINDOW_SECONDS", "60"));
public static final int SERVER_PORT = Integer.parseInt(System.getProperty("SERVER_PORT", "9090"));
public static final int SLIDING_WINDOW_SERVER_PORT = Integer.parseInt(System.getProperty("SERVER_PORT", "9091"));
public static final int THREAD_POOL_SIZE = Integer.parseInt(System.getProperty("THREAD_POOL_SIZE", "10"));


java -DRATE_LIMIT=15 -DWINDOW_SECONDS=120 -DSERVER_PORT=9090 -jar target/E_vastTel-tcp-rate-limiter-1.0-SNAPSHOT.jar
