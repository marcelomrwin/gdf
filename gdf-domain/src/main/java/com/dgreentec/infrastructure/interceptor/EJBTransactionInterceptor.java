package com.dgreentec.infrastructure.interceptor;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.transaction.Status;
import javax.transaction.TransactionSynchronizationRegistry;

import org.slf4j.Logger;

public class EJBTransactionInterceptor {

	public EJBTransactionInterceptor() {
		super();
	}

	@Inject
	protected Logger logger;

	@Resource
	TransactionSynchronizationRegistry tsr;

	@Inject
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
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
}
