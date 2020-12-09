package cn.lc.common.model;

/**
 * Created by tomas on 2017/5/24.
 */
public class CmsWebAPICode extends APICode {

    public final static int _C_USERNAME_PASSWORD_ERROR = 10001;
    public final static BaseCode USERNAME_PASSWORD_ERROR = new BaseCode(_C_USERNAME_PASSWORD_ERROR, "用户名错误");

    public final static int _C_PASSWORD_ERROR = 10002;
    public final static BaseCode PASSWORD_ERROR = new BaseCode(_C_PASSWORD_ERROR, "密码错误");

    public final static int _C_USER_DELETE = 10003;
    public final static BaseCode USER_DELETE = new BaseCode(_C_USER_DELETE, "用户被删除");

    public final static int _C_USER_DISABLE = 10004;
    public final static BaseCode USER_DISABLE = new BaseCode(_C_USER_DISABLE, "用户被禁用");

    public final static int _C_CAMPUS_DISABLE = 10005;
    public final static BaseCode CAMPUS_DISABLE = new BaseCode(_C_CAMPUS_DISABLE, "园所被禁用");

    public final static int _C_MERCHANT_DISABLE = 10006;
    public final static BaseCode MERCHANT_DISABLE = new BaseCode(_C_MERCHANT_DISABLE, "品牌被禁用");

    public final static int _C_INVALID_TOKEN = 10010;
    public final static BaseCode INVALID_TOKEN = new BaseCode(_C_INVALID_TOKEN, "Token失效");

    public final static int _C_AUTHORIZED_FAILD = 10011;
    public final static BaseCode AUTHORIZED_FAILD    = new BaseCode(_C_AUTHORIZED_FAILD, "Token认证失败");

    public final static int _C_AUTHORIZED_EXPIRE = 10012;
    public final static BaseCode AUTHORIZED_EXPIRE = new BaseCode(_C_AUTHORIZED_EXPIRE, "Token过期");



    public final static int _C_MAX_COUNT_SESSION_ERROR = 11006;
    public final static BaseCode MAX_COUNT_SESSION_ERROR = new BaseCode(_C_MAX_COUNT_SESSION_ERROR, "达到同时最大登录限制");

    public final static int _C_SECRET_KEK_ERROR = 11007;
    public final static BaseCode SECRET_KEK_ERROR = new BaseCode(_C_SECRET_KEK_ERROR, "请检查密钥是否正确");

}
