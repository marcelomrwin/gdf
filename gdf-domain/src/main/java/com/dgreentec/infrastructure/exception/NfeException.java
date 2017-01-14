package com.dgreentec.infrastructure.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NfeException extends Exception {

	private static final long serialVersionUID = -7301355708238641417L;

	public NfeException() {
		super();
	}

	public NfeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NfeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NfeException(String message) {
		super(message);
	}

	public NfeException(Throwable cause) {
		super(cause);
	}

}
