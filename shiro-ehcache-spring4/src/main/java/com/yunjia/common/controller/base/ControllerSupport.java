package com.yunjia.common.controller.base;

public abstract class ControllerSupport {

	protected final ResponseBean success(){
		return new ResponseBean().success();
	}
	protected final ResponseBean success(Object data){
		return new ResponseBean().success(data);
	}
	protected final ResponseBean failure(){
		return new ResponseBean().failure();
	}
	protected final ResponseBean hintLogin(){
		return new ResponseBean().hintLogin();
	}
	protected final ResponseBean authorizedFailure(){
		return new ResponseBean().authorizedFailure();
	}
	protected final ResponseBean failure(String msg){
		return new ResponseBean().failure(msg);
	}
}
