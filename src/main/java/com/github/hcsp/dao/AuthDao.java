package com.github.hcsp.dao;

import com.github.hcsp.entity.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthDao {
    int insertUserInfo(User user) throws DuplicateKeyException;
}
