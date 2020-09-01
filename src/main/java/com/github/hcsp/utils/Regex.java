package com.github.hcsp.utils;


import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.entity.RespJson;

public class Regex {

    private static final String usernameRegex = "[A-Z|a-z0-9_\\u4e00-\\u9fa5]{1,15}";
    private static final String passwordRegex = "\\w{6,16}";

    private Regex() {

    }

    public static RespJson isMatch(JSONObject usernameAndPassword) {
        RespJson respJson = new RespJson();
        try {
            String username = usernameAndPassword.getString("username");
            String password = usernameAndPassword.getString("password");
            if (!username.matches(usernameRegex)) {
                respJson.setSuccess(false);
                respJson.setMsg("用户名不符合规则，请仔细检查");
                return respJson;
            } else if (!password.matches(passwordRegex)) {
                respJson.setSuccess(false);
                respJson.setMsg("密码不符合规则，请仔细检查");
                return respJson;
            } else {
                respJson.setSuccess(true);
                respJson.setMsg("注册成功！");
                return respJson;
            }
        } catch (Exception e) {
            respJson.setSuccess(false);
            respJson.setMsg("用户名或密码为空，无法进行注册！");
            return respJson;
        }
    }
}
