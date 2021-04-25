package com.bulletinboard.server.services;

import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.repository.UserRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.userService = new UserService(null, userRepository);
    }


    @Test
    public void getUserById_Should_Return_User() {
        given(userRepository.findUserById(1L)).willReturn(java.util.Optional.of(new User(1L, "admin", "admin@mail.ru")));

        User userExist = userService.getUserById(1L);
        assertThat(userExist).isEqualToComparingFieldByField(new User(1L, "admin", "admin@mail.ru"));
        verify(userRepository).findUserById(1L);

    }

    @Test(expected = UsernameNotFoundException.class)
    public void getUserById_Should_Return_Exception() throws UsernameNotFoundException {
        given(userRepository.findUserById(anyLong())).willThrow(UsernameNotFoundException.class);

       userService.getUserById(1L);

    }



}
