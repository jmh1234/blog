package com.github.hcsp.service.impl;

import com.github.hcsp.dao.UserDao;
import com.github.hcsp.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private BCryptPasswordEncoder mockEncoder;

    @Mock
    private UserDao mockDao;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserInfoByUsername() {
        when(mockDao.getUserInfoByUsername("jhgfcky3")).thenReturn(new User("jhgfcky3", "123456"));
        User user = userService.getUserInfoByUsername("jhgfcky3");
        Assertions.assertEquals("jhgfcky3", user.getUsername());
        Assertions.assertEquals("123456", user.getPassword());
    }

    @Test
    void insertUserInfo() {
        when(mockEncoder.encode("123456")).thenReturn("1234567");
        userService.insertUserInfo("jhgfcky3", "123456");
        UserDao userDao = Mockito.verify(mockDao);
        userDao.insertUserInfo("jhgfcky3", "1234567");
    }

    @Test
    void throwExceptionWhenUsernameNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("jhgfcky3"));
    }

    @Test
    void returnUserDetailsWhenUsernameFound() {
        when(mockDao.getUserInfoByUsername("jhgfcky3"))
                .thenReturn(new User("jhgfcky3", "123456"));
        UserDetails user = userService.loadUserByUsername("jhgfcky3");
        Assertions.assertEquals("jhgfcky3", user.getUsername());
        Assertions.assertEquals("123456", user.getPassword());
    }
}
