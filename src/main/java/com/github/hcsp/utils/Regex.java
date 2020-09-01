package com.github.hcsp.utils;


import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.entity.LoginResult;

public class Regex {

    private static final String usernameRegex = "[A-Z|a-z0-9_\\u4e00-\\u9fa5]{1,15}";
    private static final String passwordRegex = "\\w{6,16}";

    private Regex() {

    }

    public static LoginResult isMatch(JSONObject usernameAndPassword) {
        try {
            String username = usernameAndPassword.getString("username");
            String password = usernameAndPassword.getString("password");
            if (!username.matches(usernameRegex)) {
                return LoginResult.failure("用户名不符合规则，请仔细检查!");
            } else if (!password.matches(passwordRegex)) {
                return LoginResult.failure("密码不符合规则，请仔细检查!");
            } else {
                return LoginResult.success("注册成功!", true);
            }
        } catch (Exception e) {
            return LoginResult.failure("用户名或密码为空，无法进行注册!");
        }
    }
}
