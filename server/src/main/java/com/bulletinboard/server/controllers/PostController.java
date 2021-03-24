package com.bulletinboard.server.controllers;

import com.bulletinboard.server.dto.PostDTO;
import com.bulletinboard.server.entity.Post;
import com.bulletinboard.server.facade.PostFacade;
import com.bulletinboard.server.payload.response.MessageResponse;
import com.bulletinboard.server.services.PostService;
import com.bulletinboard.server.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления постом
 * Получает http запрос и передает json ответа на клиент
 */
@RestController
@RequestMapping("api/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostFacade postFacade;
    @Autowired
    private PostService postService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;


    /**
     * Метод создания нового поста
     * @param postDTO
     * @param bindingResult
     * @param principal
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Post post = postService.createPost(postDTO, principal);
        PostDTO createPost = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(createPost, HttpStatus.OK);
    }

    /**
     * Метод для получения всех постов
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }


    /**
     * Метод для получения всех постов пользователя
     * @param principal
     * @return
     */
    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostForUser(Principal principal) {
        List<PostDTO> postDTOList = postService.getAllPostForUser(principal)
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    /**
     * Метод для добавления поста в избранное
     * @param postId
     * @param username
     * @return
     */
    @PostMapping("/{postId}/{username}/favorite")
    public ResponseEntity<PostDTO> favoritePost(@PathVariable("postId") String postId,
                                                @PathVariable("username") String username) {
        Post post = postService.favoritePost(Long.parseLong(postId), username);
        PostDTO postDTO = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }


    /**
     * Метод для удаления собственного поста
     * @param postId
     * @param principal
     * @return
     */
    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deletePost(Long.parseLong(postId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }

    /**
     * Метод для получение избранных постов
     * @return
     */
    @GetMapping("user/favorites")
    public ResponseEntity<List<PostDTO>> getFavoritePostForUser(Principal principal){
        List<PostDTO> postDTOList = postService.getFavoritePostForUser(principal)
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }


}
