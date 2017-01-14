package com.dgreentec.infrastructure.exception;

public abstract class CheckedExceptions extends Exception {

	private static final long serialVersionUID = -8535544254461357932L;

	public abstract String getKey();
}
