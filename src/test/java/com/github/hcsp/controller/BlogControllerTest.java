package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.BlogService;
import com.github.hcsp.service.impl.AuthService;
import com.github.hcsp.service.impl.BlogServiceImpl;
import com.github.hcsp.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class BlogControllerTest {
    @Mock
    private BlogService blogService;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private MockMvc userMockMvc;
    private MockMvc blogMockMvc;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        AuthService authService = new AuthService(userService);
        blogMockMvc = MockMvcBuilders.standaloneSetup(new BlogController(authService, blogService)).build();
        userMockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, authService, authenticationManager)).build();
    }

    @Test
    void create() throws Exception {
        // login info
        JSONObject userJson = new JSONObject();
        userJson.put("username", "jhgfcky3");
        userJson.put("password", "123456");

        // blog info
        JSONObject blogJson = new JSONObject();
        blogJson.put("title", "1");
        blogJson.put("content", "2");
        blogJson.put("description", "3");

        // login judge
        blogMockMvc.perform(post("/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(blogJson)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("登录后才能操作")));

        Mockito.when(userService.getUserInfoByUsername("jhgfcky3"))
                .thenReturn(new User(1, "jhgfcky3", bCryptPasswordEncoder.encode("123456")));

        // start login
        MvcResult mvcResult = userMockMvc.perform(post("/auth/login")
                .header("user-agent", "[{\"key\":\"User-Agent\",\"value\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1\",\"description\":\"\",\"type\":\"text\",\"enabled\":true}]", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(userJson)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("登录成功")))
                .andReturn();

        HttpSession httpSession = mvcResult.getRequest().getSession();
        if (httpSession == null) {
            throw new RuntimeException();
        }

        Mockito.when(blogService.addBlogInfo(new Blog())).thenReturn(BlogResult.success("博客新建成功",
                new Blog(1, "1", "2", "3", new User(1, "jhgfcky3", bCryptPasswordEncoder.encode("123456")))));

        blogMockMvc.perform(post("/blog")
                .session((MockHttpSession) httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(blogJson)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString(Charset.forName("UTF-8")));
                    Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("博客新建成功"));
                });
    }

    @Test
    void getBlogDetail() {
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