package com.github.hcsp.service.impl;

import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.service.BlogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogDao blogDao;

    @Override
    public void addBlogInfo(Blog blog) {
        blogDao.addBlogInfo(blog);
    }

    @Override
    public Blog getBlogInfoById(String blogId) {
        return blogDao.getBlogInfoById(blogId);
    }
}
