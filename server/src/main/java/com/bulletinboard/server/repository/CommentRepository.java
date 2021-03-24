package com.bulletinboard.server.repository;

import com.bulletinboard.server.entity.Comment;
import com.bulletinboard.server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Сервис для получение и сохранения комментариев в базе данных
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Comment findByIdAndUserId(Long commentId, Long userId);
}
