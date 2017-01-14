package com.dgreentec.infrastructure.exception;

public class InfraException extends RuntimeException {

	private static final long serialVersionUID = 1081400853060959272L;
	private String i18nKey;
	private Object[] arguments;

	public InfraException(String i18nKey, Object... arguments) {
		super();
		this.i18nKey = i18nKey;
		this.arguments = arguments;
	}

	public InfraException(String i18nKey, Throwable t, Object... arguments) {
		super(t);
		this.i18nKey = i18nKey;
		this.arguments = arguments;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public String getBundleKey() {
		return "exception." + getClass().getSimpleName();
	}

	public String getI18nKey() {
		return i18nKey;
	}

	/**
	 * Constrói uma nova InfraException.
	 *
	 * @param cause
	 *            Causa do erro.
	 */
	public InfraException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constrói uma nova InfraException.
	 *
	 * @param mensagem
	 *            Mensagem de erro.
	 * @param cause
	 *            Causa do erro.
	 */
	public InfraException(String mensagem, Throwable cause) {
		super(mensagem, cause);
	}

	/**
	 * Constrói uma nova InfraException com a mensagem de erro.
	 *
	 * @param mensagem
	 *            Mensagem de erro.
	 */
	public InfraException(String mensagem) {
		super(mensagem);
	}
}
