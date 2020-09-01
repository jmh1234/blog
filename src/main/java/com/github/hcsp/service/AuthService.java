package com.github.hcsp.service;

import com.github.hcsp.entity.User;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

public interface AuthService {
    void insertUserInfo(String username, String password) throws DuplicateKeyException;

    Optional<User> getCurrentUser();
}
