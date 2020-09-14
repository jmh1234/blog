package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.impl.AuthService;
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
class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private MockMvc mockMvc;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        AuthService authService = new AuthService(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, authService, authenticationManager)).build();
    }

    @Test
    void isLogin() throws Exception {
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("用户没有登录")));
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("用户没有登录")));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "jhgfcky3");
        jsonObject.put("password", "123456");

        Mockito.when(userService.getUserInfoByUsername("jhgfcky3"))
                .thenReturn(new User(1, "jhgfcky3", bCryptPasswordEncoder.encode("123456")));

        MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .header("user-agent", "[{\"key\":\"User-Agent\",\"value\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1\",\"description\":\"\",\"type\":\"text\",\"enabled\":true}]", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                .content(new ObjectMapper().writeValueAsString(jsonObject)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("登录成功"))).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
        if (session == null) {
            throw new RuntimeException();
        }
        mockMvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("jhgfcky3")));
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("用户没有登录")));

        // 登陆
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "jhgfcky3");
        jsonObject.put("password", "123456");

        Mockito.when(userService.getUserInfoByUsername("jhgfcky3"))
                .thenReturn(new User(1, "jhgfcky3", bCryptPasswordEncoder.encode("123456")));

        MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .header("user-agent", "[{\"key\":\"User-Agent\",\"value\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1\",\"description\":\"\",\"type\":\"text\",\"enabled\":true}]", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                .content(new ObjectMapper().writeValueAsString(jsonObject)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("登录成功"))).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
        if (session == null) {
            throw new RuntimeException();
        }
        mockMvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("jhgfcky3")));

        // 注销
        mockMvc.perform(get("/auth/logout")).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("用户注销成功")));

        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("用户没有登录")));
    }

    @Test
    void register() throws Exception {
        JSONObject object = new JSONObject();
        object.put("username", "jhgfcky3");
        object.put("password", "123456");

        // 注册
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.toJSONString())).andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("注册成功")));

        // 登录
        Mockito.when(userService.getUserInfoByUsername("jhgfcky3"))
                .thenReturn(new User(1, "jhgfcky3", bCryptPasswordEncoder.encode("123456")));

        MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .header("user-agent", "[{\"key\":\"User-Agent\",\"value\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1\",\"description\":\"\",\"type\":\"text\",\"enabled\":true}]", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                .content(new ObjectMapper().writeValueAsString(object)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("登录成功"))).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
        if (session == null) {
            throw new RuntimeException();
        }
        mockMvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.forName("UTF-8")).contains("jhgfcky3")));
    }
}
