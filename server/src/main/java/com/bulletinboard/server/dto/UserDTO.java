package com.bulletinboard.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {

    private Long id;
    private String username;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String bio;
}
