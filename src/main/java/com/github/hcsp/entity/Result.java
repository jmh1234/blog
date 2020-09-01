package com.github.hcsp.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Result<T> {
    public enum ResultStatus {
        OK("ok"),
        FAIL("fail");
        private String status;

        ResultStatus(String status) {
            this.status = status;
        }
    }

    private ResultStatus status;
    private String msg;
    private T data;


    Result(ResultStatus status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
