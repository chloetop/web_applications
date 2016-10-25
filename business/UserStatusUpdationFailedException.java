package edu.unsw.comp9321.business;

import edu.unsw.comp9321.common.NestedException;

public class UserStatusUpdationFailedException extends NestedException {
	
	public UserStatusUpdationFailedException(String message){
		super(message);
	}
	
	public UserStatusUpdationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public UserStatusUpdationFailedException(Throwable cause) {
		super(cause);
	}

}

