package com.source3g.hermes.exception;

public class NotLoginException extends RuntimeException{
	private static final long serialVersionUID = -7730143659728198482L;

	public NotLoginException(String message) {
		super(message);
	}
}
