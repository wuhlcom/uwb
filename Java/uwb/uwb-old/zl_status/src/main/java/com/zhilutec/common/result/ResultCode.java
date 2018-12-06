package com.zhilutec.common.result;


public enum ResultCode {

	OK(0, "OK"),
	// 成功
	SUCCESS(10001, "成功"),
	Failed(10002, "失败"),
	// 无对应数据
	NODATA_ERR(10003, "无对应数据"),
	// 数据重复插入
	REPETITION_ERR(10004, "数据重复插入"),
	
	// 未知错误
	UNKNOW_ERR(10005, "未知错误"),
	
	// 程序错误
	ROUTINE_ERR(10006, "程序错误"),
	
	// 无权限
	PERMISSION_ERR(10007, "无权限"),
	
	// 参数错误
	PARAMETER_ERR(10008, "参数错误"),
	TOKEN_ERR(10009,"Token错误");

	private int code;
	private String errmsg;

	private ResultCode(int errode, String errMsg) {
		this.code = errode;
		this.errmsg = errMsg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}