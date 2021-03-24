package com.bulletinboard.server.repository;

import com.bulletinboard.server.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Сервис для получение и сохранения изображения в базе данных
 */
@Repository
public interface ImageRepository  extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByUserId(Long userId);

    Optional<ImageModel> findByPostId(Long postId);
}
