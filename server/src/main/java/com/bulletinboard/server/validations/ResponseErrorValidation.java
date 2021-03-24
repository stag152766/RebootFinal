package com.bulletinboard.server.validations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

/**
 * Вспомогательный сервис для обработки ошибок
 */
@Service
public class ResponseErrorValidation {

    /**
     * Метод для обработки ошибки
     * BindingResult приходит из springframework.validation, содержащий ошибки (н-р, пустой пароль)
     * @param result
     * @return возвращает Map с ошибками либо null
     */
    public ResponseEntity<Object> mapValidationService(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            // проверка за объекты
            if (!CollectionUtils.isEmpty(result.getAllErrors())) {
                for (ObjectError error : result.getAllErrors()) {
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            // проверка за атрибуты
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            // если были ошибки, возвращаем ошибки и дальше не идем
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);

        }
        // если нет ошибок
        return null;

    }

}
