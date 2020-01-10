package com.sky.car.common;

public class Constant {
	
	public static final String COOKIE_KEY = "car_token";
	
    /** 返回状态--成功 */
    public static final int STATUS_SUCCESS = 0;

    /** 返回状态--失败 */
    public static final int STATUS_FAILURE = 1;

    /** 返回状态--token过期 */
    public static final int STATUS_TOKEN_INVALID = 2;
    
    /** 返回状态--参数无效 */
    public static final int STATUS_PARAM_INVALID = 3;
    
    /** 通用状态 -- 启用 */
    public static final int STATUS_ENABLED = 4;
    
    /** 通用状态 -- 禁用 */
    public static final int STATUS_DISABELD = 5;
    
    /** 通用状态 -- 更新失败 */
    public static final int STATUS_UPDATED_FAILED = 6;

    private Constant() {
    }

}
