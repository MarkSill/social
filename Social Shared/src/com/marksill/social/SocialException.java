package com.marksill.social;

/**
 * A general exception to be thrown by Social.
 */
public class SocialException extends Exception {

	private static final long serialVersionUID = 4138175624718052055L;

	/**
	 * Creates a new SocialException.
	 */
	public SocialException() {
		super();
	}

	/**
	 * Creates a new SocialException.
	 * @param msg The message for the exception.
	 */
	public SocialException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new SocialException.
	 * @param cause The cause of the exception.
	 */
	public SocialException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new SocialException.
	 * @param msg The message for the exception.
	 * @param cause The cause of the exception.
	 */
	public SocialException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Creates a new SocialException.
	 * @param msg The message for the exception.
	 * @param cause The cause of the exception.
	 * @param enableSuppression ?
	 * @param writableStackTrace ?
	 */
	public SocialException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}

}
