package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.AuthService;
import com.github.hcsp.service.BlogService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @Resource
    private AuthService authService;

    @ResponseBody
    @PostMapping("/create")
    public BlogResult create(@RequestBody JSONObject blogObject) {

        String title = blogObject.getString("title");
        String description = blogObject.getString("description");
        String content = blogObject.getString("content");
        if (StringUtils.isEmpty(title) || title.length() > 100) {
            return BlogResult.failure("博客内容不能为空，且不超过10000个字符");
        }

        if (StringUtils.isEmpty(content) || content.length() > 10000) {
            return BlogResult.failure("博客标题不能为空，且不超过100个字符");
        }

        if (StringUtils.isEmpty(description)) {
            if (content.length() < 500) {
                description = content;
            } else {
                description = content.substring(0, 500) + "...";
            }
        }

        // 获取用户ID
        Optional<User> currentUser = authService.getCurrentUser();
        if (!currentUser.isPresent()) {
            return BlogResult.failure("登录后才能操作");
        }
        Integer userId = currentUser.get().getId();
        Blog blog = new Blog(title, description, content, userId);
        blogService.addBlogInfo(blog);
        return BlogResult.success("博客新建成功");
    }

    @ResponseBody
    @GetMapping("/blogDetail/{blogId}")
    public BlogResult getBlogDetail(@PathVariable("blogId") String blogId) {
        return BlogResult.success("获取成功", blogService.getBlogInfoById(blogId));
    }
}
