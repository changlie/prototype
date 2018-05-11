package com.yunjia.common.controller.base;

import java.util.HashMap;
import java.util.Map;
/**
 * 用于客户端app的请求返回
 * @author changlie
 *
 */
public class Result {

	private int code;
	private String msg;
	private Object data;
	private String newtoken;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getNewtoken() {
		return newtoken;
	}
	public void setNewtoken(String newtoken) {
		this.newtoken = newtoken;
	}
	
	@SuppressWarnings("unchecked")
	public Result put(String key, Object value) {
		if(data ==null || !(data instanceof Map)) {
			data = new HashMap<>();
		}
		
		((Map<String, Object>) data).put(key, value);
		return this;
	}
	
	public Result success() {
		this.code = 1;
		this.msg = "请求成功！";
		return this;
	}
	public Result success(Object data) {
		this.code = 1;
		this.msg = "请求成功！";
		this.data = data;
		return this;
	}
	
	public Result failure() {
		this.code = 0;
		this.msg = "请求失败！";
		return this;
	}
	public Result failure(String msg) {
		this.code = 0;
		this.msg = msg;
		return this;
	}
	
	public Result repeatedLogin() {
		this.code = 222;
		this.msg = "重复登录！";
		return this;
	}
	public Result loseLogin() {
		this.code = 505;
		this.msg = "身份验证失效！";
		return this;
	}
	public Result loseLogin(String msg) {
		this.code = 505;
		this.msg = msg;
		return this;
	}
	
	
}
