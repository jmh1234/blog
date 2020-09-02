package com.github.hcsp.service;

import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.utils.Pagination;

import java.util.Map;

public interface BlogService {
    BlogResult addBlogInfo(Blog blog);

    Blog getBlogInfoById(int blogId);

    Pagination<Blog> getBlogListByUserId(Map<String, Integer> pageNumAndPageSize, Integer userId);

    BlogResult updateBlogById(Blog blog, int blogId);

    BlogResult deleteBlogById(int blogId);
}
