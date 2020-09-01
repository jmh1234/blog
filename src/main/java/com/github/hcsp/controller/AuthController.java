package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.aspect.AuthenticationAspect;
import com.github.hcsp.entity.RespJson;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.AuthService;
import com.github.hcsp.service.impl.UserService;
import com.github.hcsp.utils.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private UserService userService;

    @Resource
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerUtil.getInstance(AuthController.class);

    @AuthenticationAspect()
    @PostMapping("/register")
    public RespJson register(@RequestBody JSONObject usernameAndPassword, HttpServletRequest request) {
        try {
            String username = usernameAndPassword.getString("username");
            String password = usernameAndPassword.getString("password");

            authService.insertUserInfo(new User(username, password));

            return new RespJson(true, "注册成功!", login(usernameAndPassword, request));
        } catch (DuplicateKeyException duplicateKeyException) {
            return new RespJson(false, "账号重复!", null);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestBody JSONObject usernameAndPassword, HttpServletRequest request) {
        System.out.println(request.getHeader("user-agent"));
//        if (request.getHeader("user-agent") == null || !request.getHeader("user-agent").contains("Mozilla")) {
//            return "死爬虫去死吧";
//        }

        String username = usernameAndPassword.get("username").toString();
        String password = usernameAndPassword.get("password").toString();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());

        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return new RespJson(true, "登录成功!", userService.loadUserByUsername(username));
        } catch (UsernameNotFoundException e) {
            return new RespJson(false, "用户不存在!", null);
        } catch (BadCredentialsException e) {
            return new RespJson(false, "密码不正确!", null);
        }
    }
}
