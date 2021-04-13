package com.bulletinboard.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для репрезинтации объекта Пользователь в базе данных
 * также объект используется для авторизации на сервере,
 * данные которого кодируются в JWT и передаются на клиент
 * UserDetails из SECURITY
 */
@Data
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Уникальный никнейм, используется для трекинга
     * (например, показывает кто добавил в избранное)
     */
    @Column(unique = true, updatable = false)
    private String username;
    /**
     * Имя пользователя
     */
    @Column(nullable = false)
    private String name;
    /**
     * Фамилия пользователя
     */
    @Column(nullable = false)
    private String lastname;
    /**
     * Электронная почта, используется для логина (уникальное значение)
     */
    @Column(unique = true)
    private String email;
    /**
     * Допольнительная информация
     */
    @Column(columnDefinition = "text")
    private String bio;
    /**
     * Пароль
     */
    @Column(length = 3000)
    private String password;
    /**
     * Допускается наличие нескольких ролей у пользователя
     * Связи пользователь-роли хранятся в отдельной таблице
     */
    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Посты, созданные пользователем
     * ALL - для всех операций из перечисления (например, при удалении пользователя, все его посты удаляются)
     * LAZY - отложенная загрузка постов (при необходимости загружаем по get)
     */
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    /**
     * Время создания поста
     */
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;


    /**
     * Вспомогательный метод, который задает значение атрибуту @createdDate
     * до записи объекта в базу данных
     */
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }


    /**
     * Метод возвращает список ролей
     * SECURITY
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }
}


