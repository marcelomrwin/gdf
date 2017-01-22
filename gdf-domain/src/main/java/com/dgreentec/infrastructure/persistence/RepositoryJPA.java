package com.dgreentec.infrastructure.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;

@SuppressWarnings("rawtypes")
public interface RepositoryJPA extends Serializable {

	public void persist(Object entity);

	public <T extends AbstractEntityVersion> T merge(T entity);

	public void remove(Object entity);

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey);

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties);

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode);

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode,
			Map<String, Object> properties);

	public <T extends AbstractEntityVersion> T getReference(Class<T> entityClass, Object primaryKey);

	public <T extends AbstractEntityVersion> T saveOrUpdate(Class<T> cl, T entity);

	public <T extends AbstractEntityVersion> boolean exists(Class<T> cl, Serializable id);

	public void flush();

	public void setFlushMode(FlushModeType flushMode);

	public FlushModeType getFlushMode();

	public <T extends AbstractEntityVersion> void lock(T entity, LockModeType lockMode);

	public <T extends AbstractEntityVersion> void lock(T entity, LockModeType lockMode, Map<String, Object> properties);

	public <T extends AbstractEntityVersion> void refresh(T entity);

	public <T extends AbstractEntityVersion> void refresh(T entity, Map<String, Object> properties);

	public <T extends AbstractEntityVersion> void refresh(T entity, LockModeType lockMode);

	public <T extends AbstractEntityVersion> void refresh(T entity, LockModeType lockMode, Map<String, Object> properties);

	public void clear();

	public <T extends AbstractEntityVersion> void detach(T entity);

	public <T extends AbstractEntityVersion> boolean contains(T entity);

	public <T extends AbstractEntityVersion> LockModeType getLockMode(T entity);

	public void setProperty(String propertyName, Object value);

	public Map<String, Object> getProperties();

	public Query createQuery(String qlString);

	public <T extends AbstractEntityVersion> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);

	public Query createQuery(CriteriaUpdate updateQuery);

	public Query createQuery(CriteriaDelete deleteQuery);

	public <T extends AbstractEntityVersion> TypedQuery<T> createQuery(String qlString, Class<T> resultClass);

	public Query createNamedQuery(String name);

	public <T extends AbstractEntityVersion> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass);

	public Query createNativeQuery(String sqlString);

	public Query createNativeQuery(String sqlString, Class resultClass);

	public Query createNativeQuery(String sqlString, String resultSetMapping);

	public StoredProcedureQuery createNamedStoredProcedureQuery(String name);

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName);

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses);

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings);

	public void joinTransaction();

	public boolean isJoinedToTransaction();

	public <T> T unwrap(Class<T> cls);

	public Object getDelegate();

	public void close();

	public boolean isOpen();

	public EntityTransaction getTransaction();

	public EntityManagerFactory getEntityManagerFactory();

	public CriteriaBuilder getCriteriaBuilder();

	public Metamodel getMetamodel();

	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType);

	public EntityGraph<?> createEntityGraph(String graphName);

	public EntityGraph<?> getEntityGraph(String graphName);

	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass);

	public EntityManager getEntityManager();

	public void setEntityManager(EntityManager entityManager);

}
