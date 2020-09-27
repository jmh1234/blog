package com.github.hcsp.dao;

import com.github.hcsp.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User getUserInfoByUsername(String username);

    void insertUserInfo(@Param("username") String username, @Param("password") String password) throws DuplicateKeyException;
}
