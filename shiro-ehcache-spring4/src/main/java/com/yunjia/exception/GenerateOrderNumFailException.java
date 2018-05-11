package com.yunjia.exception;

public class GenerateOrderNumFailException extends RuntimeException {

	private static final long serialVersionUID = 3708676793347897779L;

	public GenerateOrderNumFailException(String message) {
        super(message);
    }
}
