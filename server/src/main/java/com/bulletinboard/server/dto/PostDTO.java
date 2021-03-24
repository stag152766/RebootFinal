package com.bulletinboard.server.dto;

import lombok.Data;

import java.util.Set;

/**
 * Объект для передачи на клиент
 * Усеченная версия поста
 */
@Data
public class PostDTO {
    private Long id;
    private String title;
    private String caption;
    private String location;
    private String username;
    private Set<String> usersFavorited;

}
