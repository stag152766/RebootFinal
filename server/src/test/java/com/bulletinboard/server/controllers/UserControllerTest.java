package com.bulletinboard.server.controllers;

import com.bulletinboard.server.dto.UserDTO;
import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.facade.UserFacade;
import com.bulletinboard.server.repository.UserRepository;
import com.bulletinboard.server.security.JWTAuthenticationEntryPoint;
import com.bulletinboard.server.security.JWTTokenProvider;
import com.bulletinboard.server.services.MyUserDetailsService;
import com.bulletinboard.server.services.UserService;
import com.bulletinboard.server.validations.ResponseErrorValidation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    // MockMvc class is used to perform API calls, but instead of doing HTTP requests,
    // Spring will test only the implementation that handle them in UserController
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserFacade userFacade;

    @MockBean
    private ResponseErrorValidation responseErrorValidation;

    @MockBean
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;


    @Test
    void getUserProfile_Should_Return_User() throws Exception {
        final Long userId = 1L;
        UserDTO userDTO = new UserDTO(userId, "admin", "admin@mail.ru");

        when(userFacade.userToUserDTO(userService.getUserById(userId))).thenReturn(userDTO);


        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{userId}", userId)
                .with(SecurityMockMvcRequestPostProcessors.user("root").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is("admin@mail.ru")))
                .andReturn().getResponse().getContentAsString();
        System.out.println("body: " + result);

    }


}
