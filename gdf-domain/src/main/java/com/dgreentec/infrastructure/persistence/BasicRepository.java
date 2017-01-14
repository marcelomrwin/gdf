package com.dgreentec.infrastructure.persistence;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
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
	@PersistenceContext
	protected EntityManager entityManager;

	@PersistenceContext(synchronization = SynchronizationType.UNSYNCHRONIZED)
	protected EntityManager unManagedEntityManager;

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
		entityManager.persist(entity);
	}

	public void merge(Object entity) {
		entityManager.merge(entity);
		flush();
	}

	public void remove(Object entity) {
		entity = entityManager.merge(entity);
		entityManager.remove(entity);
	}

	public void flush() {
		entityManager.flush();
	}

	public void setFlushMode(FlushModeType flushMode) {
		entityManager.setFlushMode(flushMode);
	}

	public FlushModeType getFlushMode() {
		return entityManager.getFlushMode();
	}

	public void clear() {
		entityManager.clear();
	}

	public Map<String, Object> getProperties() {
		return entityManager.getProperties();
	}

	public Query createQuery(String qlString) {
		return entityManager.createQuery(qlString);
	}

	public Query createQuery(CriteriaUpdate updateQuery) {
		return entityManager.createQuery(updateQuery);
	}

	public Query createQuery(CriteriaDelete deleteQuery) {
		return entityManager.createQuery(deleteQuery);
	}

	public Query createNamedQuery(String name) {
		return entityManager.createNamedQuery(name);
	}

	public Query createNativeQuery(String sqlString) {
		return entityManager.createNativeQuery(sqlString);
	}

	public Query createNativeQuery(String sqlString, Class resultClass) {
		return entityManager.createNativeQuery(sqlString, resultClass);
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return entityManager.createNativeQuery(sqlString, resultSetMapping);
	}

	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return entityManager.createNamedStoredProcedureQuery(name);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return entityManager.createStoredProcedureQuery(procedureName);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return entityManager.createStoredProcedureQuery(procedureName, resultClasses);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return entityManager.createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	public void joinTransaction() {
		entityManager.joinTransaction();
	}

	public boolean isJoinedToTransaction() {
		return entityManager.isJoinedToTransaction();
	}

	public <T> T unwrap(Class<T> cls) {
		return entityManager.unwrap(cls);
	}

	public Object getDelegate() {
		return entityManager.getDelegate();
	}

	public void close() {
		entityManager.close();
	}

	public boolean isOpen() {
		return entityManager.isOpen();
	}

	public EntityTransaction getTransaction() {
		return entityManager.getTransaction();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManager.getEntityManagerFactory();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return entityManager.getMetamodel();
	}

	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return entityManager.createEntityGraph(rootType);
	}

	public EntityGraph<?> createEntityGraph(String graphName) {
		return entityManager.createEntityGraph(graphName);
	}

	public EntityGraph<?> getEntityGraph(String graphName) {
		return entityManager.getEntityGraph(graphName);
	}

	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return entityManager.getEntityGraphs(entityClass);
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
		return entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getUnManagedEntityManager() {
		return unManagedEntityManager;
	}

	public void setUnManagedEntityManager(EntityManager unManagedEntityManager) {
		this.unManagedEntityManager = unManagedEntityManager;
	}

	public EntityManagerFactory getFactory() {
		return factory;
	}

	public void setFactory(EntityManagerFactory factory) {
		this.factory = factory;
	}
}
