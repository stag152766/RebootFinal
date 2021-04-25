package com.bulletinboard.server.controllers;

import com.bulletinboard.server.entity.Category;
import com.bulletinboard.server.facade.UserFacade;
import com.bulletinboard.server.repository.CategoryRepository;
import com.bulletinboard.server.repository.UserRepository;
import com.bulletinboard.server.security.JWTAuthenticationEntryPoint;
import com.bulletinboard.server.security.JWTTokenProvider;
import com.bulletinboard.server.services.MyUserDetailsService;
import com.bulletinboard.server.validations.ResponseErrorValidation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

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
    void getAllCategories_Should_Return_All() throws Exception {
        List<Category> categories = List.of(
                new Category(1L, "Auto"),
                new Category(2L, "Housing")
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category")
                .with(SecurityMockMvcRequestPostProcessors.user("root").roles("ADMIN"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].categoryName", Matchers.is("Auto")));

    }
}
