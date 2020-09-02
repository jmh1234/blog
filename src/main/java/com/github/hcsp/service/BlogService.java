package com.github.hcsp.service;

import com.github.hcsp.entity.Blog;

public interface BlogService {
    void addBlogInfo(Blog blog);

    Blog getBlogInfoById(String blogId);
}
