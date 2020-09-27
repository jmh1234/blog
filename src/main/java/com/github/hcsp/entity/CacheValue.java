package com.github.hcsp.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CacheValue implements Serializable {
    Object value;
    long time;

    public CacheValue(Object value, long time) {
        this.value = value;
        this.time = time;
    }
}
