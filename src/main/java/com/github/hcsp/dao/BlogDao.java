package com.github.hcsp.dao;

import com.github.hcsp.entity.Blog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogDao {

    void addBlogInfo(Blog blog);

    Blog getBlogInfoById(int blogId);

    List<Blog> getBlogListByUserId(Blog blog);

    void updateBlogById(Blog blog);

    void deleteBlogById(int blogId);
}
