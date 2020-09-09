package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.BlogService;
import com.github.hcsp.service.impl.AuthService;
import com.github.hcsp.utils.AssertUtils;
import com.github.hcsp.utils.Pagination;
import com.github.hcsp.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
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
    @PostMapping("/create")
    public BlogResult create(@RequestBody JSONObject blogObject) {
        try {
            return blogService.addBlogInfo(fromParam(blogObject));
        } catch (Exception e) {
            return BlogResult.failure("登录后才能操作");
        }
    }

    @ResponseBody
    @GetMapping("/blogDetail/{blogId}")
    public BlogResult getBlogDetail(@PathVariable("blogId") int blogId) {
        return BlogResult.success("获取成功", blogService.getBlogInfoById(blogId));
    }

    @ResponseBody
    @GetMapping("/getBlogList")
    public Pagination<Blog> getBlogList(@RequestParam("page") Integer page, @RequestParam("userId") Integer userId) {
        return blogService.getBlogListByUserId(Util.getPageNumAndPageSize(pageSize, page), userId);
    }

    @ResponseBody
    @PatchMapping("/updateBlog/{blogId}")
    public BlogResult updateBlog(@PathVariable("blogId") int blogId, @RequestBody JSONObject blogObject) {
        try {
            return blogService.updateBlogById(fromParam(blogObject), blogId);
        } catch (Exception e) {
            return BlogResult.failure("登录后才能操作");
        }
    }

    @ResponseBody
    @DeleteMapping("/deleteBlog/{blogId}")
    public BlogResult deleteBlog(@PathVariable("blogId") int blogId) {
        try {
            fromParam(new JSONObject());
            return blogService.deleteBlogById(blogId);
        } catch (Exception e) {
            return BlogResult.failure("登录后才能操作");
        }
    }

    private Blog fromParam(JSONObject params) throws Exception {
        Optional<User> currentUser = authService.getCurrentUser();
        if (!currentUser.isPresent()) {
            throw new LoginException("登录后才能操作");
        }

        String title = params.getString("title");
        String content = params.getString("content");
        String description = params.getString("description");
        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "title is invalid!");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 10000, "content is invalid");
        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }
        return new Blog(title, description, content, currentUser.get().getId());
    }
}
