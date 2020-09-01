package com.github.hcsp.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseResult {
    private String res_code;
    private String res_msg;

    @Override
    public String toString() {
        return "ResponseResult{" +
                "res_code='" + res_code + '\'' +
                ", res_msg='" + res_msg + '\'' +
                '}';
    }
}
