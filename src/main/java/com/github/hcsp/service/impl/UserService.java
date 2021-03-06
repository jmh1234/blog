package com.github.hcsp.service.impl;

import com.github.hcsp.dao.UserDao;
import com.github.hcsp.entity.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Inject
    public UserService(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserInfoByUsername(String username) {
        return userDao.getUserInfoByUsername(username);
    }

    public void insertUserInfo(String username, String password) throws DuplicateKeyException {
        userDao.insertUserInfo(username, bCryptPasswordEncoder.encode(password));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserInfoByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " 不存在！");
        }
        return new org.springframework.security.core.userdetails.User(
                username, user.getPassword(), Collections.emptyList());
    }
}
