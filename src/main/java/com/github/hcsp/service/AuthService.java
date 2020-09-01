package com.github.hcsp.service;

import com.github.hcsp.entity.User;
import org.springframework.dao.DuplicateKeyException;

public interface AuthService {
    int insertUserInfo(User user) throws DuplicateKeyException;
}
