package com.tms.constants;

import lombok.Getter;
import lombok.Setter;

public class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String REDIS_SET_ACTIVE_SUBJECTS = "active-tokens-";

    public static final String HEADER_STRING = "Authorization";

    private static String redisHost;

    private static int redisPort;

    private static String redisAuth;

    private static int redisDefaultTimeout;

    public static String getRedisHost() {
        return redisHost;
    }

    public static void setRedisHost(String redisHost) {
        SecurityConstants.redisHost = redisHost;
    }

    public static int getRedisPort() {
        return redisPort;
    }

    public static void setRedisPort(int redisPort) {
        SecurityConstants.redisPort = redisPort;
    }

    public static String getRedisAuth() {
        return redisAuth;
    }

    public static void setRedisAuth(String redisAuth) {
        SecurityConstants.redisAuth = redisAuth;
    }

    public static int getRedisDefaultTimeout() {
        return redisDefaultTimeout;
    }

    public static void setRedisDefaultTimeout(int redisDefaultTimeout) {
        SecurityConstants.redisDefaultTimeout = redisDefaultTimeout;
    }

}
