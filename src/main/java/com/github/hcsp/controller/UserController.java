package com.github.hcsp.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.entity.LoginResult;
import com.github.hcsp.service.impl.AuthService;
import com.github.hcsp.service.impl.UserService;
import com.github.hcsp.utils.LoggerUtil;
import com.github.hcsp.utils.Regex;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerUtil.getInstance(UserController.class);

    @Inject
    public UserController(UserService userService, AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @ResponseBody
    @GetMapping("/auth")
    public LoginResult isLogin() {
        return authService.getCurrentUser()
                .map(LoginResult::success)
                .orElse(LoginResult.success("用户没有登录", false));
    }

    @ResponseBody
    @GetMapping("/auth/logout")
    public LoginResult logout() {
        LoginResult ret = authService.getCurrentUser()
                .map(user -> LoginResult.success("用户注销成功", false))
                .orElse(LoginResult.failure("用户没有登录"));
        SecurityContextHolder.clearContext();
        return ret;
    }

    @ResponseBody
    @PostMapping("/auth/register")
    public LoginResult register(@RequestBody JSONObject usernameAndPassword, HttpServletRequest request) {
        try {
            String username = usernameAndPassword.getString("username");
            String password = usernameAndPassword.getString("password");
            LoginResult regex = Regex.isMatch(usernameAndPassword);
            if (regex.getIsLogin()) {
                userService.insertUserInfo(username, password);
                login(usernameAndPassword, request);
                return LoginResult.success("注册成功", userService.getUserInfoByUsername(username));
            } else {
                return regex;
            }
        } catch (DuplicateKeyException duplicateKeyException) {
            return LoginResult.failure("账号重复");
        }
    }

    @ResponseBody
    @PostMapping("/auth/login")
    public Object login(@RequestBody JSONObject usernameAndPassword, HttpServletRequest request) {
        if (request.getHeader("user-agent") == null || !request.getHeader("user-agent").contains("Mozilla")) {
            return "死爬虫去死吧";
        }
        String username = usernameAndPassword.get("username").toString();
        String password = usernameAndPassword.get("password").toString();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return LoginResult.success("登录成功", userService.getUserInfoByUsername(username));
        } catch (UsernameNotFoundException e) {
            logger.error(LoggerUtil.formatException(e));
            return LoginResult.failure("用户不存在");
        } catch (BadCredentialsException e) {
            logger.error(LoggerUtil.formatException(e));
            return LoginResult.failure("密码不正确");
        }
    }
}
