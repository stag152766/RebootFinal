package com.bulletinboard.server.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentDTO {

    private Long id;
    @NotEmpty
    private String message; // приходит с клиента
    private String username;
}
