package com.bulletinboard.server.controllers;

import com.bulletinboard.server.dto.PostDTO;
import com.bulletinboard.server.dto.UserDTO;
import com.bulletinboard.server.entity.Post;
import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.facade.PostFacade;
import com.bulletinboard.server.facade.UserFacade;
import com.bulletinboard.server.services.AdminService;
import com.bulletinboard.server.services.PostService;
import com.bulletinboard.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> userDTOList = adminService.getAllUsers()
                .stream()
                .map(userFacade::userToUserDTO)
                .collect(Collectors.toList());
        for (UserDTO user: userDTOList) {
            int count = getPostCount(user);
            user.setPostCount(count);
        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }


    private Integer getPostCount(UserDTO user){
        List<PostDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());
        int count = postDTOList
                .stream()
                .filter(p -> p.getUsername().equals(user.getUsername()))
                .collect(Collectors.toList())
                .size();

        return count;
    }



}
