package com.github.hcsp.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthDao {
    void insertUserInfo(@Param("username") String username, @Param("password") String password) throws DuplicateKeyException;
}
