package com.bulletinboard.server.payload.request;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Класс для передачи объекта на сервер для авторизации
 */
@Data
public class LoginRequest {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;


}
