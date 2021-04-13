package com.bulletinboard.server.services;

import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Вспомогательный сервис для поиска пользователя в БД
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    /**
     * Класс хранилище для взаимодействия с БД
     */
    private final UserRepository userRepository;


    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * По запросу клиента достает объект Пользователя по email из БД
     * SECURITY
     * @param email
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

        return user;
    }

    /**
     * Поиск пользователя по ИД
     * @param id
     * @return
     */
    public User loadUserById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }


}
