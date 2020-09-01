package com.github.hcsp.entity;

public class LoginResult extends Result<User> {
    private boolean isLogin;

    private LoginResult(ResultStatus status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static LoginResult success(String msg, boolean isLogin) {
        return new LoginResult(ResultStatus.OK, msg, null, isLogin);
    }

    public static LoginResult success(User user) {
        return new LoginResult(ResultStatus.OK, null, user, true);
    }

    public static LoginResult failure(String msg) {
        return new LoginResult(ResultStatus.FAIL, msg, null, false);
    }

    public static LoginResult success(String msg, User user) {
        return new LoginResult(ResultStatus.OK, msg, user, true);
    }

    public boolean getIsLogin() {
        return isLogin;
    }
}
