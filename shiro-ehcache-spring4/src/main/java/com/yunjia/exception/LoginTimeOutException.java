package com.yunjia.exception;

public class LoginTimeOutException extends RuntimeException{


	private static final long serialVersionUID = 3485730245288609170L;

	public LoginTimeOutException(String msg) {
		super(msg);
	}
	
}
