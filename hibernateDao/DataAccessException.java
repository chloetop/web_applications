package edu.unsw.comp9321.hibernateDao;


public class DataAccessException extends RuntimeException {


	/**
	 * @param message
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}