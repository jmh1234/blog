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
        when(mockDao.getUserInfoByUsername("jhgfcky3")).thenReturn(new User(1, "jhgfcky3", "123456"));
        User user = userService.getUserInfoByUsername("jhgfcky3");
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("jhgfcky3", user.getUsername());
        Assertions.assertEquals("123456", user.getPassword());
    }

    @Test
    void insertUserInfo() {
        when(mockEncoder.encode("123456")).thenReturn("123456");
        userService.insertUserInfo("jhgfcky3", "123456");
        Mockito.verify(mockDao).insertUserInfo("jhgfcky3", "123456");
    }

    @Test
    void returnUserDetailsWhenUsernameFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("jhgfcky3"));

        when(mockDao.getUserInfoByUsername("jhgfcky3"))
                .thenReturn(new User(1, "jhgfcky3", "123456"));
        UserDetails user = userService.loadUserByUsername("jhgfcky3");
        Assertions.assertEquals("jhgfcky3", user.getUsername());
        Assertions.assertEquals("123456", user.getPassword());
    }
}
