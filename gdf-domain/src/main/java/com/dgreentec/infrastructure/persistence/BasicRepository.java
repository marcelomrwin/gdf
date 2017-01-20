package com.dgreentec.infrastructure.persistence;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repositório de primeiro nível com funções básicas de persistência.
 *
 * @author marcelosales
 */
@SuppressWarnings("rawtypes")
public abstract class BasicRepository {

	/**
	 * EntityManager representa um DAO
	 */
	//	@PersistenceContext
	@Inject
	protected Instance<EntityManager> entityManager;

	//	@PersistenceContext(synchronization = SynchronizationType.UNSYNCHRONIZED)
	@Inject
	protected Instance<EntityManager> unManagedEntityManager;

	@PersistenceUnit
	protected EntityManagerFactory factory;

	@RequestScoped
	@Produces
	@RepositoryEntityManager
	public EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes @RepositoryEntityManager EntityManager manager) {
		if (manager.isOpen()) {
			manager.close();
		}
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public void persist(Object entity) {
		entityManager.get().persist(entity);
	}

	public void merge(Object entity) {
		entityManager.get().merge(entity);
		flush();
	}

	public void remove(Object entity) {
		entity = entityManager.get().merge(entity);
		entityManager.get().remove(entity);
	}

	public void flush() {
		entityManager.get().flush();
	}

	public void setFlushMode(FlushModeType flushMode) {
		entityManager.get().setFlushMode(flushMode);
	}

	public FlushModeType getFlushMode() {
		return entityManager.get().getFlushMode();
	}

	public void clear() {
		entityManager.get().clear();
	}

	public Map<String, Object> getProperties() {
		return entityManager.get().getProperties();
	}

	public Query createQuery(String qlString) {
		return entityManager.get().createQuery(qlString);
	}

	public Query createQuery(CriteriaUpdate updateQuery) {
		return entityManager.get().createQuery(updateQuery);
	}

	public Query createQuery(CriteriaDelete deleteQuery) {
		return entityManager.get().createQuery(deleteQuery);
	}

	public Query createNamedQuery(String name) {
		return entityManager.get().createNamedQuery(name);
	}

	public Query createNativeQuery(String sqlString) {
		return entityManager.get().createNativeQuery(sqlString);
	}

	public Query createNativeQuery(String sqlString, Class resultClass) {
		return entityManager.get().createNativeQuery(sqlString, resultClass);
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return entityManager.get().createNativeQuery(sqlString, resultSetMapping);
	}

	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return entityManager.get().createNamedStoredProcedureQuery(name);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return entityManager.get().createStoredProcedureQuery(procedureName);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return entityManager.get().createStoredProcedureQuery(procedureName, resultClasses);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return entityManager.get().createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	public void joinTransaction() {
		entityManager.get().joinTransaction();
	}

	public boolean isJoinedToTransaction() {
		return entityManager.get().isJoinedToTransaction();
	}

	public <T> T unwrap(Class<T> cls) {
		return entityManager.get().unwrap(cls);
	}

	public Object getDelegate() {
		return entityManager.get().getDelegate();
	}

	public void close() {
		entityManager.get().close();
	}

	public boolean isOpen() {
		return entityManager.get().isOpen();
	}

	public EntityTransaction getTransaction() {
		return entityManager.get().getTransaction();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManager.get().getEntityManagerFactory();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.get().getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return entityManager.get().getMetamodel();
	}

	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return entityManager.get().createEntityGraph(rootType);
	}

	public EntityGraph<?> createEntityGraph(String graphName) {
		return entityManager.get().createEntityGraph(graphName);
	}

	public EntityGraph<?> getEntityGraph(String graphName) {
		return entityManager.get().getEntityGraph(graphName);
	}

	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return entityManager.get().getEntityGraphs(entityClass);
	}

	protected Method findGetterMethod(Object entity, String fieldName) throws Throwable {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, entity.getClass());
		return propertyDescriptor.getReadMethod();
	}

	protected Method findSetterMethod(Object entity, String fieldName, Class<?>... parametros) throws Throwable {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, entity.getClass());
		return propertyDescriptor.getWriteMethod();
	}

	public Object getEntityId(Object entity) {
		return entityManager.get().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
	}

	public EntityManager getEntityManager() {
		return entityManager.get();
	}

	public void setEntityManager(EntityManager entityManager) {
		throw new IllegalArgumentException("CANNOT SET ENTITYMANAGER");
	}

	public EntityManager getUnManagedEntityManager() {
		return unManagedEntityManager.get();
	}

	public void setUnManagedEntityManager(EntityManager unManagedEntityManager) {
		throw new IllegalArgumentException("CANNOT SET unmanagedENTITYMANAGER");
	}

	public EntityManagerFactory getFactory() {
		return factory;
	}

	public void setFactory(EntityManagerFactory factory) {
		this.factory = factory;
	}
}
