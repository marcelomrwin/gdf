package com.dgreentec.infrastructure.model;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;

public class EntityClassUtils {

	private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

	private static final Map<String, Class<?>> commonClassCache = new HashMap<String, Class<?>>(32);

	private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(8);

	private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(32);

	static {
		primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
		primitiveWrapperTypeMap.put(Byte.class, byte.class);
		primitiveWrapperTypeMap.put(Character.class, char.class);
		primitiveWrapperTypeMap.put(Double.class, double.class);
		primitiveWrapperTypeMap.put(Float.class, float.class);
		primitiveWrapperTypeMap.put(Integer.class, int.class);
		primitiveWrapperTypeMap.put(Long.class, long.class);
		primitiveWrapperTypeMap.put(Short.class, short.class);

		for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
			primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
			registerCommonClasses(entry.getKey());
		}

		Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(32);
		primitiveTypes.addAll(primitiveWrapperTypeMap.values());
		primitiveTypes.addAll(Arrays.asList(boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class,
				long[].class, short[].class));
		primitiveTypes.add(void.class);
		for (Class<?> primitiveType : primitiveTypes) {
			primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
		}

		registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class, Float[].class, Integer[].class,
				Long[].class, Short[].class);
		registerCommonClasses(Number.class, Number[].class, String.class, String[].class, Object.class, Object[].class, Class.class,
				Class[].class);
		registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class,
				StackTraceElement[].class);
	}

	private static void registerCommonClasses(Class<?>... commonClasses) {
		for (Class<?> clazz : commonClasses) {
			commonClassCache.put(clazz.getName(), clazz);
		}
	}

	public static boolean isSimpleProperty(Class<?> clazz) {
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	public static boolean isSimpleValueType(Class<?> clazz) {
		return isPrimitiveOrWrapper(clazz) || clazz.isEnum() || CharSequence.class.isAssignableFrom(clazz)
				|| Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) || clazz.equals(URI.class)
				|| clazz.equals(URL.class) || clazz.equals(Locale.class) || clazz.equals(Class.class);
	}

	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		return primitiveWrapperTypeMap.containsKey(clazz);
	}

	public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
		for (Field f : getAllFields(clazz)) {
			if (f.getName().equals(name))
				return f;
		}

		List<Class<?>> allSuperclasses = getAllSuperclasses(clazz);
		allSuperclasses.add(clazz);
		for (Class<?> cl : allSuperclasses) {
			try {
				Field declaredField = cl.getDeclaredField(name);
				declaredField.setAccessible(true);
				return declaredField;
			} catch (Exception e) {
			}
		}

		throw new NoSuchFieldException("Field " + name + " not found in class " + clazz.getName());
	}

	public static Field[] getAllFields(List<Class<?>> classes) {
		Set<Field> fields = new HashSet<Field>();
		for (Class<?> clazz : classes) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			fields.addAll(Arrays.asList(clazz.getFields()));
		}
		return fields.toArray(new Field[fields.size()]);
	}

	public static List<Class<?>> getAllSuperclasses(Class<?> clazz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			if (DomainObject.class.isAssignableFrom(superclass))
				classes.add(superclass);
			superclass = superclass.getSuperclass();
		}

		return classes;
	}

	public static Field[] getAllFieldsIncludeDomainObjects(Class<?> clazz, String... fieldsToIgnore) {
		List<Class<?>> classes = getAllSuperclasses(clazz);
		classes.add(clazz);
		Field[] fields = getAllFields(classes);
		List<Field> lista = new LinkedList<Field>();
		for (Field f : fields) {
			if (!Collection.class.isAssignableFrom(f.getType())) {
				if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(ManyToOne.class)
						|| f.isAnnotationPresent(Embedded.class)) {
					if (!lista.contains(f) && !f.getName().equalsIgnoreCase("serialVersionUID"))
						lista.add(f);
				}
			}
		}

		return processFieldList(lista, fieldsToIgnore);
	}

	public static Field[] getAllFields(Class<?> clazz, String... fieldsToIgnore) {
		List<Class<?>> classes = getAllSuperclasses(clazz);
		classes.add(clazz);
		Field[] fields = getAllFields(classes);
		List<Field> lista = new LinkedList<Field>();
		for (Field f : fields) {
			if (!DomainObject.class.isAssignableFrom(f.getType()) && !Collection.class.isAssignableFrom(f.getType())) {
				if (f.isAnnotationPresent(Column.class) && isSimpleProperty(f.getType())) {
					if (!Collection.class.isAssignableFrom(f.getType()))
						if (!lista.contains(f) && !f.getName().equalsIgnoreCase("serialVersionUID"))
							lista.add(f);
				}
			}
		}

		return processFieldList(lista, fieldsToIgnore);
	}

	protected static Field[] processFieldList(List<Field> lista, String... fieldsToIgnore) {
		if (fieldsToIgnore != null && fieldsToIgnore.length > 0) {

			Iterator<Field> itLista = lista.iterator();
			while (itLista.hasNext()) {
				Field listField = itLista.next();
				for (String field : fieldsToIgnore) {
					if (listField.getName().equalsIgnoreCase(field))
						itLista.remove();
				}
			}
		}
		return lista.toArray(new Field[lista.size()]);
	}

	public static Field getPrimaryKeyField(Class<?> entityClass) throws PersistenceException {
		Field[] fields = getAllFields(entityClass);
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				return f;
			}
		}
		throw new PersistenceException("Primary Key Field not found!");
	}
}
