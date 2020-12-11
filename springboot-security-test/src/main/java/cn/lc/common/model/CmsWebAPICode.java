package cn.lc.common.model;

/**
 * Created by tomas on 2017/5/24.
 */
public class CmsWebAPICode extends APICode {


    public final static int _C_INVALID_TOKEN = 10010;
    public final static BaseCode INVALID_TOKEN = new BaseCode(_C_INVALID_TOKEN, "Token失效");

    public final static int _C_AUTHORIZED_FAILD = 10011;
    public final static BaseCode AUTHORIZED_FAILD    = new BaseCode(_C_AUTHORIZED_FAILD, "Token认证失败");


}
