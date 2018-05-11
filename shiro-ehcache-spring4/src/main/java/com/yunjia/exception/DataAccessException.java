package com.yunjia.exception;

public class DataAccessException extends RuntimeException {

	private static final long serialVersionUID = -1353803017844746533L;

	public DataAccessException() {
	}

	public DataAccessException(String message) {
		super(message);
	}

}
