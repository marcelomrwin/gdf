package com.dgreentec.infrastructure.repository.multitenant;

import java.lang.reflect.Proxy;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This EntityManager producer returns always a Proxy. All the EntityManager methods are wrapped by this proxy. This ensures,
 * that the real EntityManager is obtained/created at call time, not in injection time and can react to Tenant changes between
 * injection and EM method call.
 */
@RequestScoped
public class ProxyEntityManager {

	/**
	 * Inject the default EntityManager, operated by a application container. Serves as a fallback, if there is no
	 * tenant logged in and we are asked to return a EntityManager instance.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Provider of EntityManagerFactory.
	 *
	 * @see TenantRegistry#getTenant(String)
	 * @see TenantRegistry#createEntityManagerFactory(Tenant)
	 */
	@Inject
	private TenantRegistry tenantRegistry;

	private static final Logger logger = LoggerFactory.getLogger(ProxyEntityManager.class);

	/**
	 * CDI Producer. Checks if there is a tenant name in ThreadLocal storage {@link TenantHolder}. If yes, load tenant from
	 * {@link TenantRegistry},
	 * get its EntityManagerFactory. From the factory create new EntityManager, join JTA transaction and return this EntityManager.
	 *
	 * @return EntityManager for Tenant or default EntityManager, if no tenant logged in.
	 */
	@Produces
	private EntityManager getEntityManager() {
		final String currentTenant = TenantHolder.getCurrentTenant();

		final EntityManager target;

		if (currentTenant != null) {

			EntityManagerFactory emf = tenantRegistry.getEntityManagerFactory(currentTenant);
			if (emf == null)
				target = entityManager;
			else
				target = emf.createEntityManager();
		} else {
			target = entityManager;
		}
		return (EntityManager) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { EntityManager.class },
				(proxy, method, args) -> {
					target.joinTransaction();
					return method.invoke(target, args);
				});
	}
}
