package com.github.hcsp.service.impl;

import com.github.hcsp.dao.AuthDao;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.AuthService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthDao authDao;

    @Resource
    private UserService userService;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void insertUserInfo(String username, String password) throws DuplicateKeyException {
        authDao.insertUserInfo(username, bCryptPasswordEncoder.encode(password));
    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(userService.getUserInfoByUsername(authentication == null ? null : authentication.getName()));
    }
}
