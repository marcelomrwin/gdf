package com.dgreentec.infrastructure.repository;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaResolver implements CurrentTenantIdentifierResolver {
	protected Logger logger = LoggerFactory.getLogger(getClass().getName());
	public static final String MULTITENANT_ID = "MultiTenancyURLIdentifier";
	public static final String JNDI_SYNC_REGISTRY = "java:jboss/TransactionSynchronizationRegistry";

	@Override
	public String resolveCurrentTenantIdentifier() {

		TransactionSynchronizationRegistry syncRegistry;
		try {
			syncRegistry = (TransactionSynchronizationRegistry) new InitialContext().lookup(JNDI_SYNC_REGISTRY);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

		logger.debug("resolveCurrentTenantIdentifier " + syncRegistry);
		String schema = "comum";

		if (syncRegistry != null) {
			Object tenant = syncRegistry.getResource(MULTITENANT_ID);
			if (tenant != null) {
				schema = (String) tenant;
				logger.debug("TransactionSynchronizationRegistry.getResource = " + schema);
			}
		}

		return schema;

	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return false;
	}

}
