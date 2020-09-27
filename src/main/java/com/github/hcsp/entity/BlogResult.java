package com.github.hcsp.entity;

public class BlogResult extends Result<Blog> {

    private BlogResult(ResultStatus status, String msg, Blog blog) {
        super(status, msg, blog);
    }

    public static BlogResult success(String msg) {
        return new BlogResult(ResultStatus.ok, msg, null);
    }

    public static BlogResult failure(String msg) {
        return new BlogResult(ResultStatus.fail, msg, null);
    }

    public static BlogResult success(String msg, Blog blog) {
        return new BlogResult(ResultStatus.ok, msg, blog);
    }
}
