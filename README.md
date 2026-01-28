# TCP Rate Limiter with Java & Redis


# Overview

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

- Java 
- Maven
- Redis (Docker)
- Jedis 5.1.0 
- JUnit 5 (for unit tests)

## Setup & Installation
1. Run Redis using Docker Compose

## create docker-compose.yml
version: "3.9"
services:
  redis:
    image: redis:latest
    container_name: redis
    ports:
      - "6379:6379"

Command : docker-compose up -d

Redis will be available at localhost:6379.

## Configuration

You can configure rate limits, window size, ports, and thread pool size via system properties or directly in RateLimitConfig.java.

- public static final int LIMIT = Integer.parseInt(System.getProperty("RATE_LIMIT", "10"));
- public static final int WINDOW_SECONDS = Integer.parseInt(System.getProperty("WINDOW_SECONDS", "60"));
- public static final int SERVER_PORT = Integer.parseInt(System.getProperty("SERVER_PORT", "9090"));
- public static final int SLIDING_WINDOW_SERVER_PORT = Integer.parseInt(System.getProperty("SERVER_PORT", "9091"));
- public static final int THREAD_POOL_SIZE = Integer.parseInt(System.getProperty("THREAD_POOL_SIZE", "10"));


# Project Structure
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── server
│   │   │   │   ├── TcpServer.java            
│   │   │   │   └── SlidingWindowTcpServer.java
│   │   │   ├── Repository
│   │   │   │   ├── RedisRateLimitRepository.java
│   │   │   │   ├── RedisSlidingWindowRateLimitRepository.java
│   │   │   │   └── RedisConnectionFactory.java
│   │   │   └── client/configs
│   │   │       └── RateLimitConfig.java      
│   └── test
│       └── java
│           └── Repository
│               ├── RedisRateLimitRepositoryTest.java
│               └── RedisRateLimitRepositoryMultiClientTest.java
│               └── RedisSlidingWindowRateLimitRepositoryTest.java
├── pom.xml
└── README.md


# How to Use and Run the project 

## Running the TCP Servers
Before testing, decide which rate-limiting strategy you want to run:

+ **run TcpServer class for Fixed window**

+ **run SlidingWindowTcpServer class for Fixed window**

-- Make sure Redis is running (docker-compose up -d) before starting the server.

## Running clients
All test clients are in the client folder. Each client tests a specific scenario:

**1. TestClient (class)**

- Single client sends 15 requests.

- Expected: first 10 → ALLOW, next 5 → DENY.

- Purpose: basic single-client rate limiting with Fixed Window.

**2. TestClientWhenTimeGone1mReturnARequestAgain (class)**

- Single client sends 12 requests:

- First 10 → ALLOW, next 2 → DENY.

- Waits 1 minute (Fixed Window reset).

- Sends 3 additional requests → all ALLOW.

- Purpose: verify window reset behavior.

**3. TestConcurrentClients**

- 3 clients concurrently send 12 requests each:

- Each client: first 10 → ALLOW, next 2 → DENY.

- Total requests: 36 (30 ALLOW, 6 DENY).

- Purpose: test multi-client Fixed Window enforcement.

**4. TestSlidingWindowClient**

- Single client sends 15 requests:

- First 10 → ALLOW, next 5 → DENY.

- Waits 10 seconds, then sends 15 additional requests (1 request every 5 seconds):

- Requests are denied until sliding window frees a slot, then new requests start being ALLOW.

- Purpose: test Sliding Window behavior.

Note: Make sure you are connecting to the correct server port:

Fixed Window server → port 9090

Sliding Window server → port 9091

## Unit Tests

The project includes JUnit 5 tests:

- RedisRateLimitRepositoryTest.java ===>  single-client, fixed window behavior
- RedisRateLimitRepositoryMultiClientTest.java ===> multiple clients
- RedisSlidingWindowRateLimitRepositoryTest.java ===> sliding window behavior

Run all tests using Maven command: mvn test

# Rate-Limiting Algorithms

## Fixed Window
- Counts requests per client within a fixed time window (e.g., 60 seconds)
- Reset happens automatically when the window expires
- Simple but may allow bursts at window boundaries
  (ex : if you should send less than or equal 10 request per minute. 
    if client send 10 req at the end of this minute and 10 of the first of then next min 
   the client send 20 req in 1 minute).

## Sliding Window
- Tracks requests using a moving time window
- Provides smoother enforcement across the window
- More precise than fixed window


## Logging Denied Requests
 All denied requests are logged on the server console.

 ex : [pool-1-thread-1] WARN Repository.RedisRateLimitRepository - DENY request for client=/127.0.0.1:61822 count=10 timestamp=2026-01-28T02:01:24.217871500Z





## Notes

- Each client is limited independently. For example, if LIMIT=10, three clients can each send 10 requests per minute.
- Ensure Redis is running before starting the TCP server.
- This project uses raw TCP; do not attempt HTTP or REST requests.

# Author 
ziad salah
