package com.github.hcsp.service.impl;

import com.github.hcsp.dao.AuthDao;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.AuthService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthDao authDao;

    @Override
    public int insertUserInfo(User user) throws DuplicateKeyException {
        return authDao.insertUserInfo(user);
    }
}
