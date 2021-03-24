package com.bulletinboard.server.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * Класс для репрезинтации объекта Изображение в базе данных
 */
@Data
@Entity
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название изображения
     */
    @Column(nullable = false)
    private String name;
    /**
     * Изображение сохраняется в BLOB
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;

    /**
     * Изображение принадлежит либо посту, либо пользователю
     */
    @JsonIgnore
    private Long postId;
    @JsonIgnore
    private Long userId;
}
