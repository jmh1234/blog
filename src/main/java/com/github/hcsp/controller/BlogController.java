package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.aspect.AuthenticationAdvice;
import com.github.hcsp.aspect.AuthenticationAspect;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.service.BlogService;
import com.github.hcsp.utils.AssertUtils;
import com.github.hcsp.utils.Pagination;
import com.github.hcsp.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    private static final int pageSize = 5;

    @ResponseBody
    @AuthenticationAspect
    @PostMapping("/create")
    public BlogResult create(@RequestBody JSONObject blogObject) {
        return blogService.addBlogInfo(fromParam(blogObject));
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
    @AuthenticationAspect
    @PatchMapping("/updateBlog/{blogId}")
    public BlogResult updateBlog(@PathVariable("blogId") int blogId, @RequestBody JSONObject blogObject) {
        return blogService.updateBlogById(fromParam(blogObject), blogId);
    }

    @ResponseBody
    @AuthenticationAspect
    @DeleteMapping("/deleteBlog/{blogId}")
    public BlogResult deleteBlog(@PathVariable("blogId") int blogId) {
        return blogService.deleteBlogById(blogId);
    }

    private Blog fromParam(JSONObject params) {
        String title = params.getString("title");
        String content = params.getString("content");
        String description = params.getString("description");
        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "title is invalid!");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 10000, "content is invalid");
        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }
        return new Blog(title, description, content, AuthenticationAdvice.userId);
    }
}
