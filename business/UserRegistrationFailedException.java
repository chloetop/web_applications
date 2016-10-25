package edu.unsw.comp9321.business;

import edu.unsw.comp9321.common.NestedException;

public class UserRegistrationFailedException extends NestedException {
	
	public UserRegistrationFailedException(String message){
		super(message);
	}
	
	public UserRegistrationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public UserRegistrationFailedException(Throwable cause) {
		super(cause);
	}

}
