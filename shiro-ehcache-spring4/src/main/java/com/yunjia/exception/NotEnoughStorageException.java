package com.yunjia.exception;

public class NotEnoughStorageException extends RuntimeException{
	

	private static final long serialVersionUID = 3485730245288609170L;

	public NotEnoughStorageException(String msg) {
		super(msg);
	}
	
	
}
