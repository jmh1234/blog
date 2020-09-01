package com.github.hcsp.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
public class CacheKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String methodName;
    private Object thisObject;
    private Object[] arguments;

    public CacheKey(String methodName, Object thisObject, Object[] arguments) {
        this.methodName = methodName;
        this.thisObject = thisObject;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CacheKey cacheKey = (CacheKey) o;

        if (methodName != null ? !methodName.equals(cacheKey.methodName) : cacheKey.methodName != null) {
            return false;
        }
        if (thisObject != null ? !thisObject.equals(cacheKey.thisObject) : cacheKey.thisObject != null) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(arguments, cacheKey.arguments);
    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + (thisObject != null ? thisObject.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }
}
