package com.github.hcsp.dao;

import com.github.hcsp.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    User getInfoByUsername(String username);
}
