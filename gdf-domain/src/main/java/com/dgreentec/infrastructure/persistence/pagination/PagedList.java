package com.dgreentec.infrastructure.persistence.pagination;

import java.util.Iterator;
import java.util.List;

import com.dgreentec.infrastructure.model.DomainObject;

public interface PagedList<E extends DomainObject> {
	List<E> getResults();

	Long getQtdResults();

	boolean isEmpty();

	Iterator<E> iterator();

	int getCurrentSize();
}
