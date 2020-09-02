package com.github.hcsp.dao;

import com.github.hcsp.entity.Blog;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogDao {

    void addBlogInfo(Blog blog);

    Blog getBlogInfoById(String blogId);
}
