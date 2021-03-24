package com.bulletinboard.server.payload.response;

import lombok.Getter;

/**
 * Класс для возвращение объекта в случае некорректной авторизации
 * Получает ошибки из BindingResult
 */
@Getter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse(){
        this.username = "Invalid Username";
        this.password = "Invalid Password";
    }
}
