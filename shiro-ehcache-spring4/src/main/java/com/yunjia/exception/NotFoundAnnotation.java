package com.yunjia.exception;

public class NotFoundAnnotation extends RuntimeException {

	private static final long serialVersionUID = 2643701746637684561L;

	public NotFoundAnnotation(String message) {
		super(message);
	}
}
