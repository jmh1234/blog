package com.github.hcsp.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Blog {
    private int id;
    private String title;
    private String description;
    private String content;
    private Integer user_id;
    private String created_at;
    private String updated_at;
    private User user;

    public Blog(String title, String description, String content, Integer user_id) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.user_id = user_id;
    }
}
