package com.bulletinboard.server.dto;

import com.bulletinboard.server.entity.Role;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;


/**
 * Объект для передачи на клиент
 * Усеченная версия пользователя
 */
@Data
public class UserDTO {

    private Long id;
    private String username;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String bio;
    private Set<Role> roles;
}
