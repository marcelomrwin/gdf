package com.dgreentec.infrastructure.persistence;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.LockModeType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.model.DomainObject;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;

/**
 * Re-Injector Pattern Repositório de 2º nível com funções mais voltadas para objetos tipados (EntityVersion).
 *
 * @author marcelosales
 */
@SuppressWarnings("rawtypes")
public class GenericRepository extends BasicRepository implements RepositoryJPA {

	private static final long serialVersionUID = -125960157421221504L;

	protected CriteriaBuilder createCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}

	protected <E extends AbstractEntityVersion> E create(E entity) {
		entityManager.persist(entity);
		return entity;
	}

	protected <E extends AbstractEntityVersion> E update(E entity) {
		E newEntity = entityManager.merge(entity);
		return newEntity;
	}

	protected <E extends AbstractEntityVersion> void delete(E entity) {
		entityManager.remove(entityManager.merge(entity));
	}

	public <E extends AbstractEntityVersion> boolean exists(Class<E> cl, Serializable id) {
		E find = readById(cl, id);
		boolean exists = find != null;
		if (exists)
			entityManager.detach(find);
		return exists;
	}

	public <E extends AbstractEntityVersion> E saveOrUpdate(Class<E> cl, E entity) {
		Serializable id = (Serializable) getEntityId(entity);
		if (exists(cl, id)) {
			entity = readById(cl, id);
			update(entity);
		} else {
			persist(entity);
		}
		return entity;
	}

	/**
	 * Recupera uma entidade pelo seu id. é importante salientar que este método não inicializa o filtro de locale.
	 *
	 * @param cl
	 * @param id
	 * @return
	 */
	public <E extends AbstractEntityVersion> E readById(Class<E> cl, Serializable id) {
		if (id == null)
			return null;
		E entity = entityManager.find(cl, id);
		return entity;
	}

	public <E extends AbstractEntityVersion> E loadById(Class<E> cl, Serializable id) {
		if (id == null)
			return null;
		Session session = entityManager.unwrap(Session.class);
		E entity = session.load(cl, id);
		return entity;
	}

	// order
	@SuppressWarnings("unchecked")
	protected <E extends AbstractEntityVersion> void configureOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
			Root<E> root, FiltroAbstrato filtro, Join... joins) {
		Path path = filtro.findPath(filtro.getSortField(), root, joins);
		switch (filtro.getSortOrder()) {
		case ASCENDING: {
			criteriaQuery.orderBy(criteriaBuilder.asc(path));
			break;
		}
		case DESCENDING: {
			criteriaQuery.orderBy(criteriaBuilder.desc(path));
			break;
		}
		default:
			break;
		}
	}

	// pagination
	@SuppressWarnings("unchecked")
	protected <E extends AbstractEntityVersion> TypedQuery<E> createTypedQuery(CriteriaQuery<E> criteriaQuery, FiltroAbstrato<E> filtro,
			boolean useCache, EntityGraph<E>... graphs) {
		TypedQuery<E> tq = entityManager.createQuery(criteriaQuery);
		if (!filtro.isRetornarTodosOsRegistros()) {
			if (filtro.getPageSize() >= 0) {
				tq.setMaxResults(filtro.getPageSize());
			}
			if (filtro.getFirst() >= 0) {
				tq.setFirstResult(filtro.getFirst());
			}
		}

		if (useCache) {
			tq.setHint("org.hibernate.cacheable", true);
			tq.setHint("org.hibernate.cacheMode", "NORMAL");
		}

		if (graphs != null && graphs.length > 0) {
			for (EntityGraph<E> entityGraph : graphs) {
				tq.setHint("javax.persistence.fetchgraph", entityGraph);
			}
		}

		return tq;
	}

	protected <E extends AbstractEntityVersion> TypedQuery<Object[]> createObjectArrayQuery(CriteriaQuery<Object[]> criteriaQuery,
			FiltroAbstrato<E> filtro, boolean useCache) {
		TypedQuery<Object[]> tq = entityManager.createQuery(criteriaQuery);
		if (!filtro.isRetornarTodosOsRegistros()) {
			if (filtro.getPageSize() >= 0) {
				tq.setMaxResults(filtro.getPageSize());
			}
			if (filtro.getFirst() >= 0) {
				tq.setFirstResult(filtro.getFirst());
			}
		}

		if (useCache) {
			tq.setHint("org.hibernate.cacheable", true);
			tq.setHint("org.hibernate.cacheMode", "NORMAL");
		}

		return tq;
	}

	protected TypedQuery<Tuple> createTypedQueryForObjectArray(CriteriaQuery criteriaQuery, FiltroAbstrato filtro, boolean useCache) {
		TypedQuery<Tuple> tq = entityManager.createQuery(criteriaQuery);
		if (!filtro.isRetornarTodosOsRegistros()) {
			if (filtro.getPageSize() >= 0) {
				tq.setMaxResults(filtro.getPageSize());
			}
			if (filtro.getFirst() >= 0) {
				tq.setFirstResult(filtro.getFirst());
			}
		}

		if (useCache) {
			tq.setHint("org.hibernate.cacheable", true);
			tq.setHint("org.hibernate.cacheMode", "NORMAL");
		}

		return tq;
	}

	protected Long processCountQuery(CriteriaQuery<Long> qc) {
		return entityManager.createQuery(qc).getSingleResult();
	}

	public <T extends AbstractEntityVersion> T merge(T entity) {
		return entityManager.merge(entity);
	}

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		return entityManager.find(entityClass, primaryKey, properties);
	}

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return entityManager.find(entityClass, primaryKey, lockMode);
	}

	public <T extends AbstractEntityVersion> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode,
			Map<String, Object> properties) {
		return entityManager.find(entityClass, primaryKey, lockMode, properties);
	}

	public <T extends AbstractEntityVersion> T getReference(Class<T> entityClass, Object primaryKey) {
		return entityManager.getReference(entityClass, primaryKey);
	}

	public <T extends AbstractEntityVersion> void lock(T entity, LockModeType lockMode) {
		entityManager.lock(entity, lockMode);
	}

	public <T extends AbstractEntityVersion> void lock(T entity, LockModeType lockMode, Map<String, Object> properties) {
		entityManager.lock(entity, lockMode, properties);
	}

	public <T extends AbstractEntityVersion> void refresh(T entity) {
		entityManager.refresh(entity);
	}

	public <T extends AbstractEntityVersion> void refresh(T entity, Map<String, Object> properties) {
		entityManager.refresh(entity, properties);
	}

	public <T extends AbstractEntityVersion> void refresh(T entity, LockModeType lockMode) {
		entityManager.refresh(entity, lockMode);
	}

	public <T extends AbstractEntityVersion> void refresh(T entity, LockModeType lockMode, Map<String, Object> properties) {
		entityManager.refresh(entity, lockMode, properties);
	}

	public <T extends AbstractEntityVersion> void detach(T entity) {
		entityManager.detach(entity);
	}

	public <T extends AbstractEntityVersion> boolean contains(T entity) {
		return entityManager.contains(entity);
	}

	public <T extends AbstractEntityVersion> LockModeType getLockMode(T entity) {
		return entityManager.getLockMode(entity);
	}

	public void setProperty(String propertyName, Object value) {
		entityManager.setProperty(propertyName, value);
	}

	public <T extends AbstractEntityVersion> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return entityManager.createQuery(criteriaQuery);
	}

	public <T extends AbstractEntityVersion> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		return entityManager.createQuery(qlString, resultClass);
	}

	public <T extends AbstractEntityVersion> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return entityManager.createNamedQuery(name, resultClass);
	}

	protected <E extends DomainObject> Long contar(Class<E> clazz, SingularAttribute<E, Long> attr, FiltroAbstrato<E> filtro,
			SetJoin... joins) {
		CriteriaBuilder cb = unManagedEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<E> root = cq.from(clazz);
		cq.select(cb.countDistinct(root.get(attr)));
		filtro.configurarBusca(cb, cq, root, joins);
		TypedQuery<Long> tq = unManagedEntityManager.createQuery(cq);
		return tq.getSingleResult();
	}

	protected <E extends DomainObject> Long contar(CriteriaBuilder cb, CriteriaQuery<Long> cq, Root<E> root,
			SingularAttribute<E, Long> attr, FiltroAbstrato<E> filtro, SetJoin... joins) {
		cq.select(cb.countDistinct(root.get(attr)));
		filtro.configurarBusca(cb, cq, root, joins);
		TypedQuery<Long> tq = unManagedEntityManager.createQuery(cq);
		return tq.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	protected <E extends DomainObject, C extends Collection<?>> void initLazyCollections(Collection<E> collection,
			Attribute<E, C>... collectionFields) {
		try {
			for (E e : collection) {
				for (Attribute<E, C> field : collectionFields) {
					Method getter = findGetterMethod(e, field.getName());
					C c = (C) getter.invoke(e);
					c.size();
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	protected void initializeProxys(Object... proxys) {
		for (Object proxy : proxys) {
			Hibernate.initialize(proxy);
		}
	}

}
