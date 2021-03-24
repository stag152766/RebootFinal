package com.bulletinboard.server.entity;

import com.bulletinboard.server.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для репрезинтации объекта Пользователь в базе данных
 */
@Data
@Entity
public class User implements UserDetails {

    /**
     * Аннотации для валидации, которые выполняются при записи в базу данных
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Уникальный никнейм, используется для трекинга
     * (например, кто добавил в избранное)
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
     * Пароль закодирован перед записью в базе данных
     */
    @Column(length = 3000)
    private String password;

    /**
     * Допускается наличие нескольких ролей у пользователя
     * Связи пользователь-роли хранятся в отдельной таблице
     */
    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> roles = new HashSet<>();

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



    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User() {
    }

    public User(Long id,
                String username,
                String email,
                String password,
                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Вспомогательный метод, который задает значение атрибуту @createdDate
     * до записи объекта в базу данных
     */
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }


    /**
     * SECURITY
     */

    @Override
    public String getPassword() {
        return password;
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


}


