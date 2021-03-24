package com.bulletinboard.server.security;

/**
 * Вспомогательные константы для Spring Config
 */
public class SecurityConstants {

    /**
     * Используется для авторизации
     */
    public static final String SING_UP_URLS = "/api/auth/**";

    /**
     * Данные для генерации JWT токена
     */
    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_TIME = 900_000; // 15 мин
}
