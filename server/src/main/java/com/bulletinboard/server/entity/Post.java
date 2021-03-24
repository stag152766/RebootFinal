package com.bulletinboard.server.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Класс для репрезинтации объекта Пост в базе данных
 */
@Data
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Заголовок поста
      */
    private String title;
    /**
     * Краткое описание
     */
    private String caption;
    /**
     * Местоположение
     */
    private String location;
    /**
     * Количество пользователей, добавивших в избранное
     */
    private Integer favorites;
    /**
     * Список пользователей, добавивших в избранное
     */
    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> favoritedUsers = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    /**
     * Список комментариев к посту
     * @EAGER используется для загрузки всех комментов, когда загружается пост
     * @REFRESH для удаления комментов, когда удаляется пост
     */
    @OneToMany(cascade = CascadeType.REFRESH,
            fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    /**
     * Время создание поста
     */
    @Column(updatable = false)
    private LocalDateTime createdDate;

    /**
     * Вспомогательный метод, который задает значение атрибуту @createdDate
     * до записи объекта в базу данных
     */
    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }

}
