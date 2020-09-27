package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogListResult;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.BlogService;
import com.github.hcsp.service.impl.AuthService;
import com.github.hcsp.utils.AssertUtils;
import com.github.hcsp.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class BlogController {

    private final BlogService blogService;
    private final AuthService authService;
    private static final int pageSize = 5;

    @Inject
    public BlogController(AuthService authService, BlogService blogService) {
        this.authService = authService;
        this.blogService = blogService;
    }

    @ResponseBody
    @PostMapping("/blog")
    public BlogResult create(@RequestBody JSONObject blogObject) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.addBlogInfo(fromParam(blogObject, user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/blog/{blogId}")
    public BlogResult getBlogDetail(@PathVariable("blogId") int blogId) {
        return BlogResult.success("获取成功", blogService.getBlogInfoById(blogId));
    }

    @ResponseBody
    @GetMapping("/blog")
    public BlogListResult getBlogList(@RequestParam("page") Integer page, @RequestParam(value = "userId", required = false) Integer userId) {
        if (page == null || page < 0) {
            page = 1;
        }
        Blog blog = new Blog();
        blog.setUserId(userId);
        return blogService.getBlogListByUserId(Util.getPageNumAndPageSize(pageSize, page), blog);
    }

    @ResponseBody
    @PatchMapping("/blog/{blogId}")
    public BlogResult updateBlog(@PathVariable("blogId") int blogId, @RequestBody JSONObject blogObject) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlogById(blogId, fromParam(blogObject, user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping("/blog/{blogId}")
    public BlogResult deleteBlog(@PathVariable("blogId") int blogId) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.deleteBlogById(blogId, user))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e.getMessage());
        }
    }

    private Blog fromParam(JSONObject params, User user) {
        String title = params.getString("title");
        String content = params.getString("content");
        String description = params.getString("description");
        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "title is invalid!");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 10000, "content is invalid");
        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        blog.setUserId(user.getId());
        return blog;
    }
}
