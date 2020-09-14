package com.github.hcsp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String avatar;
    private String createdAt;
    private String updatedAt;

    public User(Integer id, String username, String password) {
        this.id = id;
        this.password = password;
        this.username = username;
    }
}
