package com.bulletinboard.server.facade;


import com.bulletinboard.server.dto.UserDTO;
import com.bulletinboard.server.entity.User;
import org.springframework.stereotype.Component;

/**
 * Класс для маппинга обычного объекта User и UserDTO
 */
@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setBio(user.getBio());
        return userDTO;
    }
}
