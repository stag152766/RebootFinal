package com.bulletinboard.server.services;


import com.bulletinboard.server.dto.UserDTO;
import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.exception.UserExistingException;
import com.bulletinboard.server.payload.request.SignupRequest;
import com.bulletinboard.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;


/**
 * Сервис для управления данными пользователя
 */
@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Метод для создания нового пользователя по запросу клиента
     * Пароль из браузера кодируется перед записью
     * По умолучанию роль USER, другие типы устанавливаются в базе
     *
     * @param userIn
     * @return
     */
    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));


        // запись в базу
        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(user);
            // обработка ошибки
        } catch (Exception ex) {
            LOG.error("Error during registration. {}", ex.getMessage());
            throw new UserExistingException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }


    /**
     * Метод для обновления данных пользователя
     *
     * @param userDTO
     * @param principal
     * @return
     */
    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userRepository.save(user);
    }


    /**
     * Метод для получение объекта пользователя из объекта Principal (объект Spring security)
     *
     * @param principal
     * @return
     */
    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }

    /**
     * Метод для получения текущего пользователя из объекта Principal (объект Spring security)
      * @param principal
     * @return
     */
    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    /**
     * Метод для получения пользователя по ИД
     * @param userId
     * @return
     */
    public User getUserById(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
