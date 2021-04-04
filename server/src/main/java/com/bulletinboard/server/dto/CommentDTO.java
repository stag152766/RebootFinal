package com.bulletinboard.server.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
/**
 * Объект для передачи на клиент
 * Усеченная версия комметария
 */
@Data
public class  CommentDTO {

    private Long id;
    @NotEmpty
    private String message;
    private String username;
}
