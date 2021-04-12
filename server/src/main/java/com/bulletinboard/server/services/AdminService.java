package com.bulletinboard.server.services;

import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.repository.CommentRepository;
import com.bulletinboard.server.repository.PostRepository;
import com.bulletinboard.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    public static final Logger LOG = LoggerFactory.getLogger(AdminService.class);


    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
