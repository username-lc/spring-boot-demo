package cn.lc.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class APIResult<T> implements Serializable {

    private int code;

    private boolean success = false;

    private String message;

    private T data;

    public APIResult() {
    }

    public APIResult(BaseCode baseCode) {
        this.message = baseCode.getMessage();

        int code = baseCode.getCode();
        this.code = code;

        if (code == 200) {
            this.success = true;
        }
    }

    private APIResult(BaseCode baseCode, T data) {
        this(baseCode);
        this.data = data;
    }

    public static APIResult ok() {
        return new APIResult();
    }

    public static APIResult ok(Object value) {
        return new APIResult(new BaseCode(200,""), value);
    }

    public static APIResult ok(String name, Object value) {
        return new APIResult(new BaseCode(200,""), toMap(name, value));
    }

    public static APIResult ok(Map<String, Object> data) {
        return new APIResult(new BaseCode(200,""), data);
    }

    public static APIResult error(BaseCode baseCode) {
        return new APIResult(baseCode);
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <K, V> Map<K, V> toMap(final K k, final V v) {
        return new HashMap<K, V>() {
            {
                this.put(k, v);
            }
        };
    }
}
