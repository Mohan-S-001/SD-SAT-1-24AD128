package com.ridesharing.hld.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * High-Performance In-Memory & Distributed Caching Layer.
 * Implements:
 * 1. Driver Availability & Spatial Index (Redis GEOADD / GEORADIUS equivalent)
 * 2. Driver Heartbeat with automatic 15-second TTL eviction
 * 3. Frequently Used Points of Interest (POIs) with LRU Cache
 * 4. Active Ride Session State Cache
 */
public class GeoLocationCacheService {

    public static class CachedDriverLocation {
        private final String driverId;
        private final double latitude;
        private final double longitude;
        private final long lastHeartbeatTimestamp;

        public CachedDriverLocation(String driverId, double latitude, double longitude) {
            this.driverId = driverId;
            this.latitude = latitude;
            this.longitude = longitude;
            this.lastHeartbeatTimestamp = System.currentTimeMillis();
        }

        public String getDriverId() { return driverId; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public long getLastHeartbeatTimestamp() { return lastHeartbeatTimestamp; }

        public boolean isExpired(long ttlMillis) {
            return (System.currentTimeMillis() - lastHeartbeatTimestamp) > ttlMillis;
        }
    }

    // 15 seconds TTL for driver availability
    private static final long DRIVER_HEARTBEAT_TTL_MS = 15_000L;
    private final Map<String, CachedDriverLocation> driverSpatialCache = new ConcurrentHashMap<>();

    // LRU Cache for frequently searched locations (Airports, Railway Stations, etc.)
    private final int MAX_POI_ENTRIES = 500;
    private final Map<String, String> popularLocationsCache = new LinkedHashMap<>(MAX_POI_ENTRIES, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > MAX_POI_ENTRIES;
        }
    };

    /**
     * Updates or inserts a driver's live location with TTL reset.
     * Equivalent to: REDIS GEOADD drivers:mumbai <lon> <lat> <driverId>
     * Followed by: EXPIRE driver:mumbai:<driverId> 15
     */
    public void updateDriverLocation(String driverId, double latitude, double longitude) {
        driverSpatialCache.put(driverId, new CachedDriverLocation(driverId, latitude, longitude));
    }

    /**
     * Searches nearby active drivers within a radius in kilometers, filtering out stale/expired heartbeats.
     */
    public List<String> findNearbyAvailableDriverIds(double centerLat, double centerLon, double radiusKm) {
        List<String> activeNearbyDrivers = new ArrayList<>();

        for (Map.Entry<String, CachedDriverLocation> entry : driverSpatialCache.entrySet()) {
            CachedDriverLocation loc = entry.getValue();

            // Expire driver if no GPS ping in 15 seconds
            if (loc.isExpired(DRIVER_HEARTBEAT_TTL_MS)) {
                driverSpatialCache.remove(entry.getKey());
                continue;
            }

            double dist = haversineDistance(centerLat, centerLon, loc.getLatitude(), loc.getLongitude());
            if (dist <= radiusKm) {
                activeNearbyDrivers.add(loc.getDriverId());
            }
        }

        return activeNearbyDrivers;
    }

    /**
     * Cache lookup for popular Points of Interest (POIs).
     */
    public synchronized String getCachedLocation(String query) {
        return popularLocationsCache.get(query.toLowerCase());
    }

    public synchronized void putCachedLocation(String query, String coordinatesJson) {
        popularLocationsCache.put(query.toLowerCase(), coordinatesJson);
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in KM
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
