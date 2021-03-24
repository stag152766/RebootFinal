package com.bulletinboard.server.controllers;

import com.bulletinboard.server.payload.request.LoginRequest;
import com.bulletinboard.server.payload.request.SignupRequest;

import com.bulletinboard.server.payload.response.JWTTokenSuccessResponse;
import com.bulletinboard.server.payload.response.MessageResponse;
import com.bulletinboard.server.security.JWTTokenProvider;
import com.bulletinboard.server.security.SecurityConstants;
import com.bulletinboard.server.services.UserService;
import com.bulletinboard.server.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Контроллер для принятия http запроса и авторизации пользователя
 */
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * Метод для обработки запроса на авторизацию
     * Использует кастомную валидацию mapValidationService
     * @param loginRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        // если есть ошибки, то возвращаем их и прекращаем авторизацию
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        // если ошибок нет, то авторизировать нового пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));

        // задать авторизацию
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // генерация токена
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        // возвращаем токен на клиент
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    /**
     * Метод для обработки запроса на регистрацию
     * Использует кастомную валидацию mapValidationService
     * @param signupRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        // если нет ошибок, то сохраняем пользователя
        userService.createUser(signupRequest);
        // возвращаем код 200 и сообщение
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }


}

