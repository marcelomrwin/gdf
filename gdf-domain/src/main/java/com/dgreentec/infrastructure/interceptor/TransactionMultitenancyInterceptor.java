package com.dgreentec.infrastructure.interceptor;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.transaction.TransactionSynchronizationRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.infrastructure.repository.SchemaResolver;

public class TransactionMultitenancyInterceptor {

	@Resource
	protected TransactionSynchronizationRegistry syncRegistry;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Toda operação da camada de serviço de negócio deve passar o contrato como
	 * primeiro parâmetro. O contrato é quem determina o TENANT.
	 *
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object interceptTransaction(InvocationContext ctx) throws Exception {
		logger.debug("Intercept method " + ctx.getMethod() + " from class " + ctx.getTarget().getClass().getName());

		Object[] parameters = ctx.getParameters();
		if (parameters != null) {
			Object param = parameters[0];
			if (param instanceof Contrato) {
				Contrato c = (Contrato) param;
				syncRegistry.putResource(SchemaResolver.MULTITENANT_ID, "scm_" + c.getCNPJSemFormatacao());
			}
		} else
			syncRegistry.putResource(SchemaResolver.MULTITENANT_ID, "comum");

		return ctx.proceed();
	}
}
