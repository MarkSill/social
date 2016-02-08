package com.marksill.social;

public class SocialException extends Exception {

	private static final long serialVersionUID = 4138175624718052055L;

	public SocialException() {
		super();
	}

	public SocialException(String msg) {
		super(msg);
	}

	public SocialException(Throwable cause) {
		super(cause);
	}

	public SocialException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SocialException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}

}
