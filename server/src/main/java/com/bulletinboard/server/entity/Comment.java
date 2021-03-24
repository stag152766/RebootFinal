package com.bulletinboard.server.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс для репрезинтации объекта Комментарий в базе данных
 */
@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Пост, к которому добавлен комментарий
     * У одного поста может быть много комментариев
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
    /**
     * Автор комментария
      */
    @Column(nullable = false)
    private String username;
    /**
     * ИД автора комментария
     */
    @Column(nullable = false)
    private Long userId;
    /**
     * Текст комментария
     */
    @Column(columnDefinition = "text", nullable = false)
    private String message;
    /**
     * Время добавления комментария
     */
    @Column(updatable = false)
    private LocalDateTime createdDate;


    /**
     * Вспомогательный метод, который задает значение атрибуту @createdDate
     * до записи объекта в базу данных
     */
    @PrePersist
    public void onCreate(){
        this.createdDate = LocalDateTime.now();
    }

}
