package com.github.hcsp.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Blog {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private Instant updatedAt;
    private Instant createdAt;
    private User user;

//    public Blog(String title, String description, String content, User user) {
//        this.user = user;
//        this.title = title;
//        this.content = content;
//        this.description = description;
//    }
}
