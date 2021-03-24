package com.bulletinboard.server.services;


import com.bulletinboard.server.dto.PostDTO;
import com.bulletinboard.server.entity.ImageModel;
import com.bulletinboard.server.entity.Post;
import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.exception.PostNotFoundException;
import com.bulletinboard.server.repository.ImageRepository;
import com.bulletinboard.server.repository.PostRepository;
import com.bulletinboard.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;


    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }


    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setFavorites(0);

        LOG.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);

    }


    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }


    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));
    }


    // получить все посты, созданные юзером
    public List<Post> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    // получить все избранные посты юзера
    public List<Post> getFavoritePostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user)
                .stream()
                .filter(p -> p.getFavoritedUsers().contains(user.getUsername()))
                .collect(Collectors.toList());
    }

    // добавление поста в изранное
    public Post favoritePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        // проверка поста в избранном
        Optional<String> usersFavorited = post.getFavoritedUsers()
                .stream()
                .filter(u -> u.equals(username))
                .findAny();

        // если есть, то удаляем
        if (usersFavorited.isPresent()) {
            post.getFavoritedUsers().remove(username);
        // иначе добавляем в избранное
        } else {
            post.getFavoritedUsers().add(username);
        }
        return postRepository.save(post);
    }


    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }
}
