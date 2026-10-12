package com.ridesharing.hld.scalability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token Bucket Rate Limiter implementation for API Gateway / Service Protection.
 * Used to handle sudden surges in ride requests and prevent cascading outages.
 */
public class TokenBucketRateLimiter {

    public static class Bucket {
        private final long capacity;
        private final double refillRatePerSecond;
        private double tokens;
        private long lastRefillTimestamp;

        public Bucket(long capacity, double refillRatePerSecond) {
            this.capacity = capacity;
            this.refillRatePerSecond = refillRatePerSecond;
            this.tokens = capacity;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume(int requestedTokens) {
            refill();
            if (tokens >= requestedTokens) {
                tokens -= requestedTokens;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            double secondsPassed = (now - lastRefillTimestamp) / 1000.0;
            tokens = Math.min(capacity, tokens + (secondsPassed * refillRatePerSecond));
            lastRefillTimestamp = now;
        }
    }

    private final Map<String, Bucket> clientBuckets = new ConcurrentHashMap<>();
    private final long defaultCapacity;
    private final double defaultRefillRate;

    public TokenBucketRateLimiter(long defaultCapacity, double defaultRefillRate) {
        this.defaultCapacity = defaultCapacity;
        this.defaultRefillRate = defaultRefillRate;
    }

    /**
     * Checks if the client request can be accepted or throttled with HTTP 429.
     */
    public boolean allowRequest(String clientId) {
        Bucket bucket = clientBuckets.computeIfAbsent(clientId,
                k -> new Bucket(defaultCapacity, defaultRefillRate));
        return bucket.tryConsume(1);
    }
}
