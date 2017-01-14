package com.dgreentec.infrastructure.exception;

import java.util.Arrays;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true, inherited = true)
public class BusinessException extends InfraException {

	private static final long serialVersionUID = -2416170138186603521L;
	public static final String GENERIC_MESSAGE = "generic.operation";
	public static final String GENERIC_ERROR_EXECUTE_OPERATION = "erro.execucao.operacao";
	public static final String GENERIC_CREATE_OPERATION = "generic.create.operation";
	public static final String GENERIC_RECOVERY_OPERATION = "generic.recovery.operation";
	public static final String GENERIC_UPDATE_OPERATION = "generic.update.operation";
	public static final String GENERIC_DELETE_OPERATION = "generic.delete.operation";
	public static final String DUPLICATE_KEY_ERROR = "duplicate.key.error";
	public static final String DUPLICATE_VALUE_ERROR = "duplicate.value.error";
	public static final String CONSTRAINT_VIOLATION_EXCEPTION = "constraint.violation.exception";

	public BusinessException(String i18nKey, Object... arguments) {
		super(i18nKey, arguments);
	}

	public BusinessException(String i18nKey, Throwable t, Object... arguments) {
		super(i18nKey, t, arguments);
	}

	public BusinessException(String operationKey, String[] operationParams, String detail, String i18nKey, Object... arguments) {
		super(i18nKey, arguments);
		this.operationKey = operationKey;
		this.operationParams = operationParams;
		this.detail = detail;
	}

	// guarda mensagem de titulo
	private String operationKey;
	// guarda parametros da mensagem de titulo
	private String[] operationParams;

	// guarda detalhes da mensagem de erro, exception stack trace
	private String detail;

	public String getOperationKey() {
		return operationKey;
	}

	public void setOperationKey(String operationKey) {
		this.operationKey = operationKey;
	}

	public String[] getOperationParams() {
		return operationParams;
	}

	public void setOperationParams(String[] operationParams) {
		this.operationParams = operationParams;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("BusinessException [");
		if (operationKey != null) {
			builder.append("operationKey=");
			builder.append(operationKey);
			builder.append(", ");
		}
		if (operationParams != null) {
			builder.append("operationParams=");
			builder.append(Arrays.asList(operationParams).subList(0, Math.min(operationParams.length, maxLen)));
			builder.append(", ");
		}
		if (detail != null) {
			builder.append("detail=");
			builder.append(detail);
			builder.append(", ");
		}
		if (getArguments() != null) {
			builder.append("getArguments()=");
			builder.append(Arrays.asList(getArguments()).subList(0, Math.min(getArguments().length, maxLen)));
			builder.append(", ");
		}
		if (getBundleKey() != null) {
			builder.append("getBundleKey()=");
			builder.append(getBundleKey());
			builder.append(", ");
		}
		if (getI18nKey() != null) {
			builder.append("getI18nKey()=");
			builder.append(getI18nKey());
			builder.append(", ");
		}
		if (getMessage() != null) {
			builder.append("getMessage()=");
			builder.append(getMessage());
		}
		builder.append("]");
		return builder.toString();
	}

}
