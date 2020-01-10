package com.sky.car.common;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = -1257655504253156209L;

	private int code;

	public ApiException() {

	}

	public ApiException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public ApiException(ErrorCode error) {
		super(error.getDescription());
		this.code = error.getCode();
	}

	public ApiException(ErrorCode error, String message) {
		super(message);
		this.code = error.getCode();
	}

	public int getCode() {
		return code;
	}

}
