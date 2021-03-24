package com.bulletinboard.server.services;

import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Вспомогательный сервис для поиска пользователя в базе данных
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    /**
     * Класс хранилище для взаимодействия с базой данных
     */
    private final UserRepository userRepository;


    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * По запросу клиента возвращает объект Пользователя из базы данных по username
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return build(user);
    }

    /**
     * Поиск пользователя по ИД
     * @param id
     * @return
     */
    public User loadUserById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }


    /**
     * Вспомогательный метод, который строит объект Пользователя
     * @param user
     * @return
     */
    public static User build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

}
