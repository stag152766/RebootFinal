package com.bulletinboard.server.payload.response;

import lombok.Data;

/**
 * Сообщение от сервера
 */
@Data
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}

