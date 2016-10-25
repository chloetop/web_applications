package edu.unsw.comp9321.business;

import edu.unsw.comp9321.common.NestedException;

public class ItemsFetchingFailedException extends NestedException {
	
	/**
	 * @param message
	 */
	public ItemsFetchingFailedException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ItemsFetchingFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ItemsFetchingFailedException(Throwable cause) {
		super(cause);
	}


}
