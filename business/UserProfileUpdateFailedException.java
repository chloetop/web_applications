package edu.unsw.comp9321.business;

import edu.unsw.comp9321.common.NestedException;

public class UserProfileUpdateFailedException extends NestedException {
	
	public UserProfileUpdateFailedException(String message){
		super(message);
	}
	
	public UserProfileUpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public UserProfileUpdateFailedException(Throwable cause) {
		super(cause);
	}

}

