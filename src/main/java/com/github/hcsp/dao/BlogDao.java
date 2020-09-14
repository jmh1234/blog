package com.github.hcsp.dao;

import com.github.hcsp.entity.Blog;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogDao {

    void addBlogInfo(Blog blog);

    Blog getBlogInfoById(int blogId);

    Page<Blog> getBlogListByUserId(Blog blog);

    void updateBlogById(Blog blog);

    void deleteBlogById(int blogId);
}
