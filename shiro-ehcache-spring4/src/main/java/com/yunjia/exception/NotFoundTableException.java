package com.yunjia.exception;

public class NotFoundTableException extends RuntimeException{


	private static final long serialVersionUID = -5245455855235874909L;

	public NotFoundTableException(String msg) {
		super(msg);
	}
	
}
