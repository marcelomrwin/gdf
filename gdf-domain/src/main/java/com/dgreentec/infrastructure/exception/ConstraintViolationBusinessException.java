package com.dgreentec.infrastructure.exception;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.ApplicationException;
import javax.validation.ConstraintViolation;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@SuppressWarnings("unchecked")
@ApplicationException(rollback = true)
public class ConstraintViolationBusinessException extends BusinessException {

	private static final long serialVersionUID = 7734961274400490442L;

	public ConstraintViolationBusinessException(String i18nKey, Object[] arguments) {
		super(i18nKey, arguments);
	}

	@SuppressWarnings("rawtypes")
	private List<ConstraintViolation> exceptions = new LinkedList<ConstraintViolation>();

	public <T extends AbstractEntityVersion> ConstraintViolationBusinessException(Set<ConstraintViolation<T>> exceptions,
			Object... arguments) {
		this(CONSTRAINT_VIOLATION_EXCEPTION, arguments);
		for (ConstraintViolation<T> cv : exceptions) {
			this.exceptions.add(cv);
		}

	}

	public <T extends AbstractEntityVersion> List<ConstraintViolation<T>> getExceptions() {
		List<ConstraintViolation<T>> set = new ArrayList<ConstraintViolation<T>>();
		for (ConstraintViolation<?> cv : exceptions) {
			set.add((ConstraintViolation<T>) cv);
		}
		return set;
	}

}
