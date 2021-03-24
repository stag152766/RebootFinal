package com.bulletinboard.server.payload.response;


import lombok.Data;

/**
 * Возвращаем объект
 */
@Data
public class JWTTokenSuccessResponse {

    private boolean success;
    private String token;

    public JWTTokenSuccessResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }
}

