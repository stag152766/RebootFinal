package com.bulletinboard.server.facade;

import com.bulletinboard.server.dto.PostDTO;
import com.bulletinboard.server.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setId(post.getId());
        postDTO.setCaption(post.getCaption());
        postDTO.setLocation(post.getLocation());
        postDTO.setTitle(post.getTitle());
        postDTO.setUsersFavorited(post.getFavoritedUsers());

        return postDTO;
    };
}
