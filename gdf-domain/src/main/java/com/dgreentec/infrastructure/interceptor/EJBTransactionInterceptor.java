package com.dgreentec.infrastructure.interceptor;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.TransactionSynchronizationRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJBTransactionInterceptor {

	public EJBTransactionInterceptor() {
		super();
	}

	Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	TransactionSynchronizationRegistry tsr;

	@PersistenceContext
	protected EntityManager entityManager;

	@Resource
	protected SessionContext context;

	@AroundInvoke
	public Object verifyFlush(InvocationContext invocationContext) throws Exception {
		try {
			Object obj = invocationContext.proceed();
			if (Status.STATUS_NO_TRANSACTION != tsr.getTransactionStatus()) {
				if (entityManager.isJoinedToTransaction()) {
					entityManager.flush();
				}
			}
			return obj;
		} catch (Exception e) {
			// TODO encapsular a exceção e devolver mensagem padronizada.
			logger.debug(e.getMessage(), e);
			throw e;
		}
	}
}
