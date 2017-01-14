package com.dgreentec.infrastructure.persistence.filter;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang3.StringUtils;

import com.dgreentec.infrastructure.model.DomainObject;
import com.dgreentec.infrastructure.model.EntityClassUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class FiltroAbstrato<E extends DomainObject> implements Serializable {

	private static final long serialVersionUID = 1787113569966299240L;

	public enum SortOrder {
		ASCENDING, DESCENDING, UNSORTED;
	}

	public enum JoinMode {
		AND, OR;
	}

	protected Class<E> entityClass;

	protected Integer pageSize = 10;

	protected Integer first = 0;

	protected String sortField = null;

	protected SortOrder sortOrder = SortOrder.ASCENDING;

	protected boolean retornarTodosOsRegistros = false;

	protected boolean useCount = true;

	protected EntityManager entityManager;

	private List<String> selectFields = new ArrayList<>();

	protected boolean listRootAttributes = true;

	public void addSelectField(String... fields) {
		selectFields.clear();
		for (String field : fields) {
			selectFields.add(field);
		}
		if (selectFields.isEmpty())
			listRootAttributes = true;
		else
			listRootAttributes = false;
	}

	public List<String> getSelectFields() {
		if (listRootAttributes) {
			getPrimitiveFields(entityClass);
		}
		return Collections.unmodifiableList(selectFields);
	}

	public List<Path<?>> configureSelect(CriteriaQuery<Tuple> cq, Root<E> root) {
		List<Path<?>> paths = new ArrayList<>();
		List<Selection<?>> selections = new ArrayList<>();
		try {
			final String alias = entityClass.getSimpleName().toLowerCase();
			E entity = (E) entityClass.newInstance();

			// separar atributos primitivos dos relacionamentos
			for (String field : getSelectFields()) {
				// trata relacionamentos
				if (field.contains(".")) {
					String[] joins = StringUtils.split(field, ".");

					Iterator<String> joinIterator = Arrays.asList(joins).iterator();
					Path<?> path = null;
					String rootJoin = joinIterator.next();
					Join currentJoin = root.join(rootJoin, JoinType.LEFT);
					currentJoin.alias(rootJoin);

					while (joinIterator.hasNext()) {
						String join = joinIterator.next();
						if (joinIterator.hasNext()) {
							currentJoin = currentJoin.join(join, JoinType.LEFT);
							currentJoin.alias(join);
						} else {
							path = currentJoin.get(join);
							path.alias(join);
						}
					}
					selections.add(path);
					paths.add(path);
				}
				// trata campos da entidade
				else {
					Method m = findGetterMethod(root, field);
					if (Collection.class.isAssignableFrom(m.getReturnType())) {

						ParameterizedType p = (ParameterizedType) m.getGenericReturnType();
						Class<?> cl = (Class<?>) p.getActualTypeArguments()[0];
						Join<Object, Object> join = root.join(field);
						join.alias(field);
						Set<String> primitives = returnPrimitiveFields(cl);
						for (String joinPrimitive : primitives) {
							Path<Object> path = join.get(joinPrimitive);
							path.alias(field + "." + joinPrimitive);
							paths.add(path);
							selections.add(path);
						}

					} else {
						Path<?> path = root.get(field);
						path.alias(field);
						selections.add(path);
						paths.add(path);
					}
				}
			}

			cq.multiselect(selections);
			return paths;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public final Method findGetterMethod(Object entity, String fieldName) throws Throwable {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, entityClass);
		return propertyDescriptor.getReadMethod();
	}

	public final Method findSetterMethod(Object entity, String fieldName, Class<?>... parametros) throws Throwable {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, entityClass);
		return propertyDescriptor.getWriteMethod();
	}

	private void getPrimitiveFields(Class c) {
		Field[] fields = getAllFields(c);
		for (Field f : fields) {
			if (!DomainObject.class.isAssignableFrom(f.getType())) {
				if (f.isAnnotationPresent(Column.class) && EntityClassUtils.isSimpleProperty(f.getType())) {
					if (!Collection.class.isAssignableFrom(f.getType()))
						if (!selectFields.contains(f.getName()))
							selectFields.add(f.getName());
				}

				if (f.isAnnotationPresent(Embedded.class)) {
					getPrimitivesForAlias(f.getName(), f.getType());
				}
			}
		}
	}

	private Set<String> returnPrimitiveFields(Class c) {
		Set<String> selecteds = new HashSet<>();
		Field[] fields = getAllFields(c);
		for (Field f : fields) {
			if (!DomainObject.class.isAssignableFrom(f.getType())) {
				if (f.isAnnotationPresent(Column.class) && EntityClassUtils.isSimpleProperty(f.getType())) {
					if (!Collection.class.isAssignableFrom(f.getType()))
						selecteds.add(f.getName());
				}

				if (f.isAnnotationPresent(Embedded.class)) {
					selecteds.addAll(returnPrimitivesForAlias(f.getName(), f.getType()));
				}
			}
		}
		return selecteds;
	}

	private void getPrimitivesForAlias(String pAlias, Class c) {
		Field[] fields = getAllFields(c);
		for (Field f : fields) {
			if (!DomainObject.class.isAssignableFrom(f.getType())) {
				if (f.isAnnotationPresent(Column.class) && EntityClassUtils.isSimpleProperty(f.getType())) {
					if (!Collection.class.isAssignableFrom(f.getType()))
						if (!selectFields.contains(f.getName()))
							selectFields.add(pAlias + "." + f.getName());
				}
			}
		}
	}

	private Set<String> returnPrimitivesForAlias(String pAlias, Class c) {
		Set<String> selecteds = new HashSet<>();
		Field[] fields = getAllFields(c);
		for (Field f : fields) {
			if (!DomainObject.class.isAssignableFrom(f.getType())) {
				if (f.isAnnotationPresent(Column.class) && EntityClassUtils.isSimpleProperty(f.getType())) {
					if (!Collection.class.isAssignableFrom(f.getType()))
						selecteds.add(pAlias + "." + f.getName());
				}
			}
		}

		return selecteds;
	}

	protected Field[] getAllFields(Class<?> clazz) {
		List<Class<?>> classes = getAllSuperclasses(clazz);
		classes.add(clazz);
		return getAllFields(classes);
	}

	private Field[] getAllFields(List<Class<?>> classes) {
		Set<Field> fields = new HashSet<Field>();
		for (Class<?> clazz : classes) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}
		return fields.toArray(new Field[fields.size()]);
	}

	protected List<Class<?>> getAllSuperclasses(Class<?> clazz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			if (DomainObject.class.isAssignableFrom(superclass))
				classes.add(superclass);
			superclass = superclass.getSuperclass();
		}

		return classes;
	}

	public abstract boolean isFiltroAlterado();

	protected abstract void limpar();

	protected boolean isStringValida(String pString) {
		return StringUtils.isNotEmpty(pString) && StringUtils.isNotBlank(pString);
	}

	public FiltroAbstrato() {
		super();
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
	}

	protected void updateProperty(String propertyName, Object value) {
		try {
			boolean valid = false;
			if (value instanceof String)
				valid = isStringValida((String) value);
			else
				valid = value != null;
			Field fieldProperty = getField(propertyName);
			Field fieldPropertyUpdated = getField(propertyName + "Alterado");
			if (valid) {
				fieldProperty.set(this, value);
				fieldPropertyUpdated.set(this, Boolean.TRUE);
			} else {
				fieldProperty.set(this, null);
				fieldPropertyUpdated.set(this, Boolean.FALSE);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Field getField(String pField) throws Exception {
		Field field = getClass().getDeclaredField(pField);
		field.setAccessible(Boolean.TRUE);
		return field;
	}

	protected CriteriaQuery<?> cq;

	public void limparFiltro() {
		sortField = null;
		retornarTodosOsRegistros = false;
		limpar();
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isRetornarTodosOsRegistros() {
		return retornarTodosOsRegistros;
	}

	public void setRetornarTodosOsRegistros(boolean retornarTodosOsRegistros) {
		this.retornarTodosOsRegistros = retornarTodosOsRegistros;
	}

	public final void configurarBusca(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Root<E> root, Join... joins) {

		if (isFiltroAlterado()) {
			cq = criteriaQuery;
			Predicate where = configureWhereClause(criteriaBuilder, root, joins);
			criteriaQuery.where(where);
			cq = null;
		}

	}

	protected abstract Predicate configureWhereClause(CriteriaBuilder criteriaBuilder, Root<E> root, Join... joins);

	public abstract Path<?> findPath(String field, Root<E> root, Join... collection);

	public boolean isUseCount() {
		return useCount;
	}

	public void setUseCount(boolean useCount) {
		this.useCount = useCount;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
