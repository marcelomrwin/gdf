package com.dgreentec.infrastructure.service.boundary;

import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dgreentec.infrastructure.exception.BusinessException;
import com.dgreentec.infrastructure.exception.ConstraintViolationBusinessException;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;

public abstract class AbstractBoundary {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	protected Instance<TransactionSynchronizationRegistry> registry;

	@Inject
	protected Validator validator;

//	@PersistenceContext

//	protected EntityManager em;

//	protected void flushCurrentEntityManager() {
//		em.flush();
//	}

	protected UserTransaction lookupUserTransaction() {
		UserTransaction ut = null;
		try {
			ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return ut;
	}

	protected BusinessException tratarExcecao(Throwable t, String key) {
		return new BusinessException(key, t);
	}

	protected BusinessException tratarExcecao(String key, Object... params) {
		return new BusinessException(key, params);
	}

	protected BusinessException tratarExcecao(String message, Throwable t, Object... params) {
		return new BusinessException(BusinessException.GENERIC_ERROR_EXECUTE_OPERATION, new String[] { "" },
				ExceptionUtils.getStackTrace(t), message, params);
	}

	protected <T extends AbstractEntityVersion> void validate(T entity) {
		Set<ConstraintViolation<T>> exceptions = validator.validate(entity);
		if (exceptions != null && !exceptions.isEmpty())
			throw new ConstraintViolationBusinessException(exceptions);
	}
}
