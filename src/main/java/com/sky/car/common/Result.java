package com.sky.car.common;

import com.sky.car.common.Constant;
import com.sky.car.common.ErrorCode;
import com.sky.car.common.Result;

public class Result{

    //返回状态  0成功  1失败 2ken过期
    private int code;
    private String msg;
    private Object data;

    public Result() {

    }

    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    public static Result successResult() {
        return Result.successResult("success", null);
    }

    public static Result successResult(Object data) {
        return Result.successResult("success", data);
    }

	public static Result successResult(String msg, Object data) {
        return new Result(Constant.STATUS_SUCCESS, msg, data);
    }
    
    /*****************错误信息***************/
	
	public static Result failResult(Object data) {
	    return Result.failResult("failure", data);
	}
	  
	public static Result tokenInvalidResult() {
	    return Result.failResult(Constant.STATUS_TOKEN_INVALID,"登录失效，请重新登录", null);
	}
	
	public static Result tokenInvalidResult(String msg) {
	    return Result.failResult(Constant.STATUS_TOKEN_INVALID, msg, null);
	}
    
    public static Result failResult() {
        return new Result(Constant.STATUS_FAILURE, null, null);
    }
    
    public static Result failResult(String msg) {
        return new Result(Constant.STATUS_FAILURE, msg, null);
    }

    public static Result failResult(String msg, Object data) {
        return new Result(Constant.STATUS_FAILURE, msg, data);
    }
    
    public static Result failResult(int status ,String msg, Object data) {
        return new Result(status, msg, data);
    }
    
    public static Result failResult(ErrorCode error) {
        return new Result(error.getCode(), error.getMessage(), null);
    }
    
    public static Result failResult(ErrorCode error ,String msg) {
        return new Result(error.getCode(), msg, null);
    }
    
    public static Result failResult(ErrorCode error ,String msg,Object data) {
        return new Result(error.getCode(), msg, data);
    }
    
    /** 其他错误信息 **/
    
    public static Result badArgumentResult() {
    		return Result.failResult("param bad");
    }
    
    public static Result paramInvalidResult(String msg) {
    		return new Result(Constant.STATUS_PARAM_INVALID, msg, null);
    }
    
    public static Result paramInvalidResult() {
		return new Result(Constant.STATUS_PARAM_INVALID, "param invalid", null);
    }
    
    public static Result updatedFailed(String msg) {
		return new Result(Constant.STATUS_UPDATED_FAILED, msg, null);
    }

    public static Result updatedFailed() {
    		return new Result(Constant.STATUS_UPDATED_FAILED, "update failed", null);
    }
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String error) {
        this.msg = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
