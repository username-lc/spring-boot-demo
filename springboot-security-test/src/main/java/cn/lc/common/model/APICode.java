package cn.lc.common.model;

public class APICode {

    public final static int _C_OK = 200 ;
    public final static BaseCode OK = new BaseCode(_C_OK, null);

    public static final int _C_PASSWORD_ERROR = 31002;
    public static final BaseCode PASSWORD_ERROR = new BaseCode(_C_PASSWORD_ERROR, "密码错误");

    public final static int _C_ACCESS_DENIED = 403;
    public final static BaseCode ACCESS_DENIED  = new BaseCode(_C_ACCESS_DENIED, "访问被拒绝");

    public final static int _C_NOT_FOUND = 404 ;
    public final static BaseCode NOT_FOUND = new BaseCode(_C_NOT_FOUND, "资源不存在");

    public final static int _C_SERVER_ERROR = 500;
    public final static BaseCode SERVER_ERROR = new BaseCode(_C_SERVER_ERROR, "请求处理异常");

    public final static int _C_PARAMETER_ERROR = 400;
    public final static BaseCode PARAMETER_ERROR = new BaseCode(_C_PARAMETER_ERROR, "参数错误");

    public final static int _C_UNAUTHORIZED = 401;
    public final static BaseCode UNAUTHORIZED = new BaseCode(_C_UNAUTHORIZED, "未认证");

    public final static int _C_INVALID_TOKEN = 402;
    public final static BaseCode INVALID_TOKEN = new BaseCode(_C_INVALID_TOKEN, "无效的Token");

    public final static int _C_REMOTE_SERVER_ERROR = 600;
    public final static BaseCode REMOTE_SERVER_ERROR = new BaseCode(_C_REMOTE_SERVER_ERROR, "远程服务器错误");

    public final static int _C_ALREADY_EXISTS = 700;
    public final static BaseCode ALREADY_EXISTS = new BaseCode(_C_ALREADY_EXISTS, "数据已存在");

    public final static int _C_RELATION_EXISTS = 701;
    public final static BaseCode RELATION_EXISTS = new BaseCode(_C_ALREADY_EXISTS, "关系已存在");

    public final static int _C_NOT_EXISTS = 800;
    public final static BaseCode NOT_EXISTS = new BaseCode(_C_NOT_EXISTS, "数据不存在 ");

    public final static int _C_OPERATE_ERROR = 1001;
    public final static BaseCode OPERATE_ERROR = new BaseCode(_C_OPERATE_ERROR, "操作失败");

    public final static int _C_CAN_NOT_DEL = 1002;
    public final static BaseCode CAN_NOT_DEL = new BaseCode(_C_CAN_NOT_DEL, "无法删除");

    public final static int _C_NO_DATA_ERROR = 1003;
    public final static BaseCode NO_DATA_ERROR = new BaseCode(_C_NO_DATA_ERROR, "无数据");

    public final static int _C_IMPORT_ERROR = 1004;
    public final static BaseCode IMPORT_ERROR = new BaseCode(_C_IMPORT_ERROR, "导入失败");

    public final static int _TIMETABLE_ALREADY_EXISTS = 1005;
    public final static BaseCode TIMETABLE_ALREADY_EXISTS = new BaseCode(_TIMETABLE_ALREADY_EXISTS, "课程表已存在");

    public final static int _TIMETABLE_NOT_EXISTS = 1006;
    public final static BaseCode TIMETABLE_NOT_EXISTS = new BaseCode(_TIMETABLE_NOT_EXISTS, "课程表不存在");

    public final static int _CODE_ALREADY_EXISTS = 1007;
    public final static BaseCode CODE_ALREADY_EXISTS = new BaseCode(_CODE_ALREADY_EXISTS, "code已存在");

    public final static int _MISSING_CONFIGURATION_INFORMATION = 1008;
    public final static BaseCode MISSING_CONFIGURATION_INFORMATION = new BaseCode(_MISSING_CONFIGURATION_INFORMATION, "缺少配置信息");

    public final static int _CURRENTTERM_NOT_EXISTS = 1009;
    public final static BaseCode CURRENTTERM_NOT_EXISTS = new BaseCode(_CURRENTTERM_NOT_EXISTS, "当前学期信息不存在");

    public final static int _CURRENTYEAR_NOT_EXISTS = 1009;
    public final static BaseCode CURRENTYEAR_NOT_EXISTS = new BaseCode(_CURRENTYEAR_NOT_EXISTS, "当前学年信息不存在");

    public final static int _C_USER_PERMISSION_ERROR = 10020;
    public final static BaseCode USER_PERMISSION_ERROR = new BaseCode(_C_USER_PERMISSION_ERROR, "用户没有权限操作");

    // cms flag code
    public final static int _CMS_WEB_FLAG = 10000;

    // vo flag code
    public final static int _USER_SERVICE_FLAG = 20000;

    // api flag code
    public final static int _API_WEB_FLAG = 30000;

    // product flag code
    public final static int _PRODUCT_SERVICE_FLAG = 40000;

    // trade flag code
    public final static int _TRADE_SERVICE_FLAG = 50000;

    public static BaseCode ok(String message) {
        return new BaseCode(_C_OK, message);
    }

    public static BaseCode error(String message) {
        return new BaseCode(_C_PARAMETER_ERROR, message);
    }

}