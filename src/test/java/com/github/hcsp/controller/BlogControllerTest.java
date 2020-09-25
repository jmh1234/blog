package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.BlogService;
import com.github.hcsp.service.impl.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class BlogControllerTest {
    @Mock
    private BlogService blogService;
    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BlogController(authService, blogService)).build();
    }

    @Test
    void createWithoutLogin() throws Exception {
        JSONObject blogJson = new JSONObject();
        blogJson.put("title", "1");
        blogJson.put("content", "2");
        blogJson.put("description", "3");
        mockMvc.perform(post("/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(blogJson)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("登录后才能操作")));
    }

    @Test
    void createBlog() throws Exception {
        JSONObject blogJson = new JSONObject();
        blogJson.put("title", "1");
        blogJson.put("content", "2");
        blogJson.put("description", "3");
        Mockito.when(authService.getCurrentUser()).thenReturn(Optional.of(new User(1, "mockUser", "")));

        Mockito.when(blogService.addBlogInfo(any())).thenReturn(BlogResult.success("博客新建成功", null));
        mockMvc.perform(post("/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(blogJson)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString(Charset.forName("UTF-8")));
                    Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("博客新建成功"));
                });
    }

    @Test
    void getBlogDetail() throws Exception {
        Mockito.when(blogService.getBlogInfoById(anyInt())).thenReturn(new Blog(
                1, "", "", "", new User(1, "mockUser", "")
        ));
        mockMvc.perform(get("/blog/1"))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("获取成功")));
    }

    @Test
    void getBlogList() {
    }

    @Test
    void updateBlog() {
    }

    @Test
    void deleteBlog() {
    }
}
