package com.github.hcsp.service;

import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogListResult;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;

import java.util.Map;

public interface BlogService {
    BlogResult addBlogInfo(Blog blog);

    Blog getBlogInfoById(int blogId);

    BlogListResult getBlogListByUserId(Map<String, Integer> pageNumAndPageSize, Blog blog);

    BlogResult updateBlogById(int blogId, Blog blog);

    BlogResult deleteBlogById(int blogId, User user);
}
