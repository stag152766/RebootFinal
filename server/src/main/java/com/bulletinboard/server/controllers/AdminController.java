package com.bulletinboard.server.controllers;

import com.bulletinboard.server.dto.PostDTO;
import com.bulletinboard.server.dto.UserDTO;
import com.bulletinboard.server.facade.PostFacade;
import com.bulletinboard.server.facade.UserFacade;
import com.bulletinboard.server.services.AdminService;
import com.bulletinboard.server.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private PostService postService;
    @Autowired
    private PostFacade postFacade;

    /**
     * Метод возвращает список всех пользователей
     *
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOList = adminService.getAllUsers()
                .stream()
                .map(userFacade::userToUserDTO)
                .collect(Collectors.toList());
        for (UserDTO user : userDTOList) {
            int count = getPostCount(user);
            int total = getAmount(user);
            user.setPostCount(count);
            user.setTotalAmount(total);
        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }


    /**
     * Вспомогательный метод для подсчета количества постов пользователя
     *
     * @param user
     * @return
     */
    private Integer getPostCount(UserDTO user) {
        return postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .filter(p -> p.getUsername().equals(user.getUsername()))
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Вспомогательный метод для подсчета суммы товаров пользователя
     *
     * @param user
     * @return
     */
    private Integer getAmount(UserDTO user) {
        int total = 0;
        List<PostDTO> posts = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .filter(p -> p.getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());
        for (PostDTO post: posts){
            total += post.getPrice();
        }
        return total;
    }


}
