package com.dgreentec.infrastructure.repository.multitenant;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dgreentec.domain.model.Tenant;

/**
 * Loads Tenants from DB, creates EntityManagerFactories for them.
 */
@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TenantRegistry {

	/**
	 * Default, container managed EntityManager
	 */
	@PersistenceContext
	private EntityManager entityManager;

	private final Set<Tenant> tenants = new HashSet<>();

	private final Map<String, EntityManagerFactory> entityManagerFactories = new HashMap<>();

	@Inject
	private Logger logger;

	@PostConstruct
	protected void startupTenants() {
		final List<Tenant> tenants = loadTenantsFromDB();
		logger.info(String.format("Loaded %d tenants from DB.", tenants.size()));
		tenants.forEach(tenant -> {
			registerTenant(tenant);
		});
		this.tenants.addAll(tenants);
	}

	public void registerTenant(Tenant tenant) {
		this.tenants.add(tenant);
		final EntityManagerFactory emf = createEntityManagerFactory(tenant);
		entityManagerFactories.put(tenant.getSchemaName(), emf);
		logger.info("Tenant " + tenant.getSchemaName() + " loaded.");
	}

	@PreDestroy
	protected void shutdownTenants() {
		entityManagerFactories.forEach((tenantName, entityManagerFactory) -> entityManagerFactory.close());
		entityManagerFactories.clear();
		tenants.clear();
	}

	private List<Tenant> loadTenantsFromDB() {
		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tenant> q = cb.createQuery(Tenant.class);
		final Root<Tenant> c = q.from(Tenant.class);
		q.select(c);
		final TypedQuery<Tenant> query = entityManager.createQuery(q);
		return query.getResultList();
	}

	/**
	 * Create new {@link EntityManagerFactory} using this tenant's schema.
	 *
	 * @param tenant
	 *            Tenant used to retrieve schema name
	 * @return new EntityManagerFactory
	 */
	private EntityManagerFactory createEntityManagerFactory(final Tenant tenant) {
		return doCreateEntityManagerFactory(tenant.getSchemaName());
	}

	private EntityManagerFactory doCreateEntityManagerFactory(final String schemaName) {
		final Map<String, String> props = new TreeMap<>();
		logger.debug("Creating entity manager factory on schema ' " + schemaName + "' for tenant '" + schemaName + "'.");
		props.put("hibernate.default_schema", schemaName);
		props.put("javax.persistence.schema-generation.database.action", "none");
		props.put("javax.persistence.schema-generation.scripts.action", "none");
		props.put("hibernate.ejb.entitymanager_factory_name", schemaName + "Factory");

		String oldXMLInputFactory = System.getProperty("javax.xml.stream.XMLInputFactory");
		String oldXMLOutputFactory = System.getProperty("javax.xml.stream.XMLOutputFactory");
		String oldXMLEventFactory = System.getProperty("javax.xml.stream.XMLEventFactory");

		EntityManagerFactory emf = null;
		try {

			System.setProperty("javax.xml.stream.XMLInputFactory", "__redirected.__XMLInputFactory");
			System.setProperty("javax.xml.stream.XMLOutputFactory", "__redirected.__XMLOutputFactory");
			System.setProperty("javax.xml.stream.XMLEventFactory", "__redirected.__XMLEventFactory");

			emf = Persistence.createEntityManagerFactory("gdf-pu", props);
		} finally {
			System.setProperty("javax.xml.stream.XMLInputFactory", oldXMLInputFactory);
			System.setProperty("javax.xml.stream.XMLOutputFactory", oldXMLOutputFactory);
			System.setProperty("javax.xml.stream.XMLEventFactory", oldXMLEventFactory);
		}
		return emf;
	}

	public Optional<Tenant> getTenant(final String tenantName) {
		return tenants.stream().filter(tenant -> tenant.getSchemaName().equals(tenantName)).findFirst();
	}

	/**
	 * Returns EntityManagerFactory from the cache. EMF is created during tenant registration and initialization.
	 *
	 * @see #startupTenants()
	 */
	public EntityManagerFactory getEntityManagerFactory(final String tenantSchemaName) {
		if (tenantSchemaName == null)
			return null;
		EntityManagerFactory factory = entityManagerFactories.get(tenantSchemaName);

		debug("return entityManagerFactory for tenant " + tenantSchemaName);

		return factory;
	}

	protected void debug(String text) {
		System.out.println("** DEBUG BEGIN ** |" + getClass().getName() + ":" + Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " | " + new Date() + " | " + text + "| ** DEBUG END **");
	}
}
