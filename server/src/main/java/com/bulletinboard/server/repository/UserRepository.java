package com.bulletinboard.server.repository;

import com.bulletinboard.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Сервис для получение и сохранения пользователей в базе данных
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    // альтернатива findUserByUsername
    Optional<User> getUserByUsername(String username);


}
