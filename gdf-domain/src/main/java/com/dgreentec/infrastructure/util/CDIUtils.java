package com.dgreentec.infrastructure.util;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

public class CDIUtils {

	private static final CDIUtils instance = new CDIUtils();

	private CDIUtils() {
	}

	public static CDIUtils getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBeanInstance(final Class<T> type, final Class<? extends Annotation> scope, final BeanManager beanManager) {
		final Context context = beanManager.getContext(scope);
		final Set<Bean<?>> beans = beanManager.getBeans(type);
		final Bean<T> bean = (Bean<T>) beanManager.resolve(beans);
		final CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);

		return context.get(bean, creationalContext);
	}

	public <T> T getBeanInstance(final Class<T> clazz) {
		BeanManager bm = CDI.current().getBeanManager();
		Bean<T> bean = (Bean<T>) bm.getBeans(clazz).iterator().next();
		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		T instance = (T) bm.getReference(bean, clazz, ctx);
		return instance;
	}
}
