package com.github.hcsp.service.impl;

import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogListResult;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.utils.Util;
import com.github.pagehelper.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @Mock
    private BlogDao mockDao;

    @InjectMocks
    private BlogServiceImpl blogService;

    @Test
    void addBlogInfo() {
    }

    @Test
    void getBlogInfoById() {
        Mockito.when(mockDao.getBlogInfoById(1)).thenReturn(new Blog(
                1, "这是一篇博客", "就不告诉你里面有什么介绍",
                "这里面的的内容多着那，不好意思就不告诉你了",
                new User(1, "jhgfcky3", "123456")
        ));

        Blog blog = blogService.getBlogInfoById(1);
        Assertions.assertEquals(1, blog.getId());
        Assertions.assertEquals("这是一篇博客", blog.getTitle());
        Assertions.assertEquals("就不告诉你里面有什么介绍", blog.getDescription());
        Assertions.assertEquals("这里面的的内容多着那，不好意思就不告诉你了", blog.getContent());
        Assertions.assertEquals(1, blog.getUser().getId());
        Assertions.assertEquals("jhgfcky3", blog.getUser().getUsername());
        Assertions.assertEquals("123456", blog.getUser().getPassword());
    }

    @Test
    void getBlogListByUserId() {
        Blog blog = new Blog();
        blog.setUserId(1);
        List<Blog> blogList = Arrays.asList(Mockito.mock(Blog.class), Mockito.mock(Blog.class),
                Mockito.mock(Blog.class), Mockito.mock(Blog.class));
        Page<Blog> page = new Page<>();
        page.addAll(blogList);
        page.setTotal(4);

        Mockito.when(mockDao.getBlogListByUserId(blog)).thenReturn(page);
        BlogListResult result = blogService.getBlogListByUserId(Util.getPageNumAndPageSize(3, 1), blog);
        verify(mockDao).getBlogListByUserId(blog);

        Assertions.assertEquals(1, result.getPage());
        Assertions.assertEquals(4, result.getTotal());
        Assertions.assertEquals(2, result.getTotalPage());
        Assertions.assertEquals("获取成功", result.getMsg());
    }

    @Test
    void returnFailureWhenExceptionThrown() {
        when(mockDao.getBlogListByUserId(any())).thenThrow(new RuntimeException());
        BlogListResult result = blogService.getBlogListByUserId(Util.getPageNumAndPageSize(3, 1), null);
        Assertions.assertEquals("fail", result.getStatus().toString());
        Assertions.assertEquals("系统异常", result.getMsg());
    }

    @Test
    void updateBlogWhenNotFound() {
        when(mockDao.getBlogInfoById(anyInt())).thenReturn(null);
        BlogResult blogResult = blogService.updateBlogById(1, new Blog());
        Assertions.assertEquals("博客不存在", blogResult.getMsg());
    }

    @Test
    void updateBlogWhenIsNotYourSelf() {
        when(mockDao.getBlogInfoById(1)).thenReturn(new Blog(
                null, null, null, null,
                new User(1, "MyUser", "")
        ));
        Blog blog = new Blog();
        blog.setUserId(2);
        BlogResult blogResult = blogService.updateBlogById(1, blog);
        Assertions.assertEquals("无法修改别人的博客", blogResult.getMsg());
    }

    @Test
    void updateBlogById() {
        when(mockDao.getBlogInfoById(1)).thenReturn(new Blog(
                null, null, null, null,
                new User(1, "MyUser", "")
        ));

        Blog blog = new Blog();
        blog.setUserId(1);
        blog.setId(1);
        BlogResult blogResult = blogService.updateBlogById(1, blog);

        verify(mockDao).updateBlogById(blog);
        Assertions.assertEquals("修改成功", blogResult.getMsg());
    }
}
