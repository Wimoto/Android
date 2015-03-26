package com.mobitexoft.utils.db;

/**
 * Ver.1.0
 */
public class SQLiteBaseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SQLiteBaseException() {
    }

    public SQLiteBaseException(String message) {
        super(message);
    }

    public SQLiteBaseException(Throwable cause) {
        super(cause);
    }

    public SQLiteBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}