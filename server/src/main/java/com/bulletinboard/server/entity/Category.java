package com.bulletinboard.server.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс для репрезинтации Категории в базе данных
 */
@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название категории
     */
    private String categoryName;

}
