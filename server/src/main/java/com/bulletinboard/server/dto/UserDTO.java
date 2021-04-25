package com.bulletinboard.server.dto;

import com.bulletinboard.server.entity.Role;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
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
    private String email;
    private Integer postCount;

    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username) && Objects.equals(firstname, userDTO.firstname) && Objects.equals(lastname, userDTO.lastname) && Objects.equals(bio, userDTO.bio) && Objects.equals(roles, userDTO.roles) && Objects.equals(email, userDTO.email) && Objects.equals(postCount, userDTO.postCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, lastname, bio, roles, email, postCount);
    }
}
