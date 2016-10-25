package edu.unsw.comp9321.business;

import edu.unsw.comp9321.common.NestedException;

public class GetUserFailedException extends NestedException {

	/**
	 * @param message
	 */
	public GetUserFailedException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GetUserFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public GetUserFailedException(Throwable cause) {
		super(cause);
	}

}
