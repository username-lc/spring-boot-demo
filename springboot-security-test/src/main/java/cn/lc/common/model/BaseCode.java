package cn.lc.common.model;

import com.alibaba.fastjson.JSON;

public class BaseCode {

    protected int code;

    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public BaseCode() {}

    public BaseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static void main(String[] args) {
        BaseCode example = new BaseCode(200, "success");
        System.out.print(JSON.toJSONString(example));
    }

}
