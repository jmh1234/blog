package com.github.hcsp.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户后台向前台返回的JSON对象
 */
@Setter
@Getter
public class RespJson implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger Logger = LoggerFactory.getLogger(RespJson.class);

    private String msg = "";
    private Object data = null;
    private boolean success = false;

    public RespJson() {
    }

    public RespJson(Boolean success, String msg, Object obj) {
        this.success = success;
        this.msg = msg;
        this.data = obj;
    }


    @Override
    public String toString() {
        return "RespJson [success=" + success + ", msg=" + msg + ", data=" + data + "]";
    }
}
