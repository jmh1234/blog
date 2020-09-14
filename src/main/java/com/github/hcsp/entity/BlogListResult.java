package com.github.hcsp.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlogListResult extends Result<List<Blog>> {
    private int total;
    private int page;
    private int totalPage;

    public static BlogListResult success(List<Blog> data, int total, int page, int totalPage) {
        return new BlogListResult(ResultStatus.ok, "获取成功", data, total, page, totalPage);
    }

    public static BlogListResult failure(String msg) {
        return new BlogListResult(ResultStatus.fail, msg, null, 0, 0, 0);
    }

    private BlogListResult(ResultStatus status, String msg, List<Blog> data, int total, int page, int totalPage) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "BlogListResult{" +
                "total=" + total +
                ", page=" + page +
                ", totalPage=" + totalPage +
                '}';
    }
}
