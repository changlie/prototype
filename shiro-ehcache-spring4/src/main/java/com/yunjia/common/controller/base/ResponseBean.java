package com.yunjia.common.controller.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于web请求的返回
 * @author changlie
 *
 */
public class ResponseBean {
	
	// private static final int common_success_result = 1;
	// private static final int common_failure_result = 0;
	// private static final int login_success_result = 666;
	// private static final int login_failure_result = 555;
	// private static final int authorized_failure_result = 505;
	
	private int result;
	private String msg;
	private Object data;
	private Map<String, Object> map;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public ResponseBean setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public Object getData() {
		return data;
	}
	public ResponseBean setData(Object data) {
		this.data = data;
		return this;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public ResponseBean put(String key, Object value){
		if(map==null){
			map = new HashMap<>();
		}
		map.put(key, value);
		return this;
	}
	
	public ResponseBean success(){
		this.result = 1;
		this.msg = "";
		this.data = "";
		return this;
	}
	public ResponseBean success(Object data){
		this.result = 1;
		this.msg = "";
		this.data = data;
		return this;
	}
	
	public ResponseBean failure(){
		this.result = 0;
		this.msg = "";
		this.data = "";
		return this;
	}
	
	
	public ResponseBean failure(String msg){
		this.result = 0;
		this.msg = msg;
		this.data = "";
		return this;
	}
	
	
	public ResponseBean authorizedFailure(){
		this.result = 505;
		this.msg = "权限不足！";
		this.data = "";
		return this;
	}
	
	public ResponseBean hintLogin(){
		this.result = 404;
		this.msg = "请登录！";
		this.data = "";
		return this;
	}
	
}
