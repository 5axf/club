package com.sky.car.common;


/**
 * 错误编码定义
 */
public enum ErrorCode {
	
    requestParamError(1001,"request Param Error","请求参数错误"), 
    
    // 系统默认异常
    SystemError(10000, "System Error", "系统错误"),


    /***
     * 错误的HTTP方法
     */
    UnsupportMethod(10002, "Unsupport Method ", "错误的HTTP方法"),

    /**
     * 没有这个API
     */
    NoSuchAPI(10003, "No Such API", "没有这个API"),

    /***
     * 错误的URI,没有找到对应的路由
     */
    NoRoute(10004, "No Permission", "错误的URI,没有找到对应的路由"),

    /**
     * 权限不足
     */
    NoPermission(10005, "No Permission", "权限不足"),

    /**
     * Session已经过期
     */
    SESSIONISEXPIRED(10006, "Sessiion Is Expired", "Session已经过期"),

    /**
     * 用户未登录
     */
    NOTLOGN(10007, "User not login", "用户未登录"),

    /**
     * 未知错误
     */
    UnknownError(10008, "Unknown Error", "未知错误"),

    /***
     * 解析失败
     */
    JsonError(10009, "Json ErrorJson", "解析失败"),

    /***
     * 参数错误
     */
    ParamsError(10010, "Params Error", "参数错误"),

    /*****
     * 无效的用户
     */
    InvalidUser(10011, "Invalid User", "无效的用户"),

    /***
     * 访问的资源不存在
     */
    NotFound(10012, "NotFound", "访问的资源不存在"),

    /**
     * 错误的请求
     */
    BadRequest(10013, "BadRequest", "错误的请求"),

    /**
     * 内容不能为空
     **/
    NOTNULL(10014, "Should Not NULL", "参数，内容不能为空"),
    /**
     * 数据权限不足
     */
    NoDataPermission(10015, "No DATA Permission", "数据权限不足")
    ;

    private int code;
    private String message;
    private String description;

    private ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

