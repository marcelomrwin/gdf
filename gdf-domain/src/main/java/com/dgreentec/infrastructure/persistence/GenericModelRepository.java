package com.dgreentec.infrastructure.persistence;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.PersistenceException;
import javax.persistence.Table;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.dgreentec.infrastructure.exception.BusinessException;
import com.dgreentec.infrastructure.model.AbstractEntityVersion;
import com.dgreentec.infrastructure.model.EntityClassUtils;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

/**
 * Repositório de 3º nível com funções especificas para entidades de negócio.
 *
 * @author marcelosales
 * @param <T>
 */
public abstract class GenericModelRepository<T extends AbstractEntityVersion> extends GenericRepository implements ModelRepositoryJPA<T> {

	@SuppressWarnings("unchecked")
	public GenericModelRepository() {
		super();
		Type type = getClass().getGenericSuperclass();

		while (!(type instanceof ParameterizedType) || ((ParameterizedType) type).getRawType() != GenericModelRepository.class) {
			if (type instanceof ParameterizedType) {
				type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
			} else {
				type = ((Class<?>) type).getGenericSuperclass();
			}
		}

		this.entityClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
	}

	private static final long serialVersionUID = -8369434497538498133L;

	protected Class<T> entityClass;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <I extends Serializable, C extends Collection<I>> T consultarPorChavePrimariaInitCollections(Serializable id,
			Attribute... initFields) throws PersistenceException {
		try {
			T t = super.readById(entityClass, id);
			if (t != null) {
				for (Attribute field : initFields) {
					Method getter = findGetterMethod(t, field.getName());
					Object invoked = getter.invoke(t);
					Collection<I> col = (Collection<I>) invoked;
					col.size();
				}
			}
			return t;
		} catch (Throwable e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public T adicionar(T entity) {
		// validar chave duplicada
		Serializable id = entity.getIdValue();
		if (id != null) {
			if (exists(entityClass, id))
				throw new BusinessException(BusinessException.GENERIC_CREATE_OPERATION, new String[] { entityClass.getSimpleName() },
						"Duplicate Key [" + id + "]", BusinessException.DUPLICATE_KEY_ERROR, id, "cadastro." + entityClass.getName());
		}

		validateUniqueKey(entity, new CheckConstraintSpecification() {

			@Override
			public boolean validate(List<Serializable> result, AbstractEntityVersion entity) {
				return result.isEmpty() && result.size() <= 0;
			}
		}, BusinessException.GENERIC_CREATE_OPERATION);

		super.create(entity);

		id = entity.getIdValue();
		if (id != null)
			entity = consultarPorChave(id);

		return entity;
	}

	protected void validateUniqueKey(T entity, CheckConstraintSpecification specification, String operationKey) {
		// validar atributos marcados como unique
		Field[] entityFields = EntityClassUtils.getAllFields(entityClass);
		Table table = entityClass.getAnnotation(Table.class);
		UniqueConstraint[] uniqueConstraints = table.uniqueConstraints();
		List<String> columnNames = new ArrayList<>();
		if (uniqueConstraints != null && uniqueConstraints.length > 0) {
			for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
				String[] cols = uniqueConstraint.columnNames();
				if (cols != null && cols.length > 0) {
					for (String col : cols) {
						columnNames.add(col);
					}
				}
			}
		}

		for (Field field : entityFields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column cl = field.getAnnotation(Column.class);
				//
				if (columnNames.contains(cl.name())) {
					CriteriaBuilder cb = entityManager.getCriteriaBuilder();
					CriteriaQuery<Serializable> cq = cb.createQuery(Serializable.class);
					Root<T> from = cq.from(entityClass);
					try {
						Object fieldValue = entity.getFieldValue(field.getName());
						Predicate where = cb.equal(from.get(field.getName()), fieldValue);
						cq.select(from.get(entity.getIdField().getName())).where(where);
						TypedQuery<Serializable> query = entityManager.createQuery(cq);
						List<Serializable> result = query.getResultList();
						if (!specification.validate(result, entity))
							throw new BusinessException(operationKey, new String[] { entityClass.getSimpleName() },
									"Duplicate Value [" + fieldValue + "]", BusinessException.DUPLICATE_VALUE_ERROR, fieldValue,
									"cadastro." + entityClass.getName());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
						throw new RuntimeException(e);
					}
				}
				//
			}
		}
	}

	@Override
	public T atualizar(T entity) {

		validateUniqueKey(entity, new CheckConstraintSpecification() {

			@Override
			public boolean validate(List<Serializable> result, AbstractEntityVersion entity) {
				if (!result.isEmpty()) {
					if (result.size() > 1)
						return false;
					Serializable next = result.iterator().next();
					if (!next.equals(entity.getIdValue()))
						return false;
				}
				return true;
			}
		}, BusinessException.GENERIC_UPDATE_OPERATION);

		T nEntity = super.update(entity);

		Serializable id = nEntity.getIdValue();
		nEntity = consultarPorChave(id);

		return nEntity;
	}

	@Override
	public T atualizar(T entity, boolean flush) {
		super.update(entity);
		if (flush)
			entityManager.flush();

		entity = consultarPorChave(entity.getIdValue());

		return entity;
	}

	@Override
	public void excluir(T entity) {
		super.remove(entity);
	}

	@Override
	public T consultarPorChave(Serializable key) {
		return super.readById(entityClass, key);
	}

	@Override
	public T buscarPorChave(Serializable key) {
		return super.loadById(entityClass, key);
	}

	public PagedList<T> listar(FiltroAbstrato<T> filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<T> root = cq.from(entityClass);
		root.alias(entityClass.getSimpleName().toLowerCase());
		// select
		List<Path<?>> selection = filtro.configureSelect(cq, root);
		// order
		configureOrder(cb, cq, root, filtro);
		// filter
		filtro.configurarBusca(cb, cq, root);
		// pagination
		TypedQuery<Tuple> tq = createTypedQueryForObjectArray(cq, filtro, true);

		// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<T> rootCount = qc.from(entityClass);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<T> list = resolveTupleQuery(tq, selection);

		Long count = processCountQuery(qc);

		return new PageEntityList<>(list, count);
	}

	public List<T> resolveTupleQuery(TypedQuery<Tuple> tq, List<Path<?>> selection) {

		try {
			List<Tuple> resultObjects = tq.getResultList();
			List<T> list = new ArrayList<>();

			if (!resultObjects.isEmpty()) {

				for (Tuple tuple : resultObjects) {
					T entity = entityClass.newInstance();

					for (Path<?> path : selection) {
						Object obj = tuple.get(path.getAlias());
						updatePropertyPath(entity, obj, path);
					}

					list.add(entity);
				}
			}

			return list;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	protected void updatePropertyPath(T t, Object value, Path<?> path) {
		Object root = updateParentPath(t, path);
		Bindable<?> model = path.getModel();
		if (model instanceof SingularAttribute) {
			SingularAttribute<?, ?> attr = (SingularAttribute) model;
			Member member = attr.getJavaMember();
			if (member instanceof Field) {
				Field field = (Field) member;
				try {
					Method setter = findSetterMethod(root, field.getName(), field.getDeclaringClass());
					setter.invoke(root, value);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private Object updateParentPath(Object root, Path<?> path) {
		try {
			Path<?> current = path.getParentPath();
			if (current != null && !(current instanceof ListJoin)) {
				Object newRoot = root;
				List<Path<?>> paths = new ArrayList<>();
				do {
					paths.add(current);
					current = current.getParentPath();
				} while (current != null);
				// remove o último (normalmente o root)
				paths.remove(paths.size() - 1);

				Collections.reverse(paths);

				for (Path<?> p : paths) {
					Bindable<?> model = p.getModel();
					if (model instanceof SingularAttribute) {
						SingularAttribute<?, ?> attr = (SingularAttribute) model;
						Member member = attr.getJavaMember();
						if (member instanceof Field) {

							Field field = (Field) member;
							Method getter = findGetterMethod(newRoot, field.getName());
							Object invoked = getter.invoke(newRoot);
							if (invoked == null) {
								Class<?> returned = getter.getReturnType();
								if (Collection.class.isAssignableFrom(returned)) {
									// instanciar uma coleção
								} else {
									Object instance = returned.newInstance();
									Method setter = findSetterMethod(newRoot, field.getName(), field.getDeclaringClass());
									setter.invoke(newRoot, instance);
									newRoot = instance;
								}
							} else {
								newRoot = invoked;
							}
						}
					}
				}

				return newRoot;
			} else if (current != null && (current instanceof ListJoin)) {
				ListJoin join = (ListJoin) current;
				Class javaType = join.getJavaType();
				System.out.println(javaType);
				ListAttribute model = join.getModel();
				System.out.println(model);
			}

			return root;

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
