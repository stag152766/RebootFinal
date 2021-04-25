package com.bulletinboard.server.controllers;

import com.bulletinboard.server.entity.Category;
import com.bulletinboard.server.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления постом
 * Получает http запрос и передает json ответа на клиент
 */
@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Метод для возвращения всех категорий
     * @return
     */
    @GetMapping(value = "/api/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<List<Category>> getAllPosts() {
        List<Category> categories = categoryRepository.findAll()
                .stream()
                .collect(Collectors.toList());

        return new ResponseEntity<>(categories, HttpStatus.OK);
        //return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(categories);
    }
}
