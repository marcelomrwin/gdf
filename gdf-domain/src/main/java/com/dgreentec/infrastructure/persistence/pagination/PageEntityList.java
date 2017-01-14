package com.dgreentec.infrastructure.persistence.pagination;

import java.util.Iterator;
import java.util.List;

import com.dgreentec.infrastructure.model.DomainObject;

public class PageEntityList<E extends DomainObject> extends PageList implements PagedList<E> {

	public PageEntityList(List<E> results, Long count) {
		super();
		this.results = results;
		this.setQtdResults(count);
	}

	public PageEntityList() {
		super();
	}

	private List<E> results;

	public List<E> getResults() {
		return results;
	}

	public void setResults(List<E> results) {
		this.results = results;
	}

	@Override
	public boolean isEmpty() {
		if (results != null) {
			return results.isEmpty();
		}
		return true;
	}

	@Override
	public Iterator<E> iterator() {
		if (results != null) {
			return results.iterator();
		}
		return null;
	}

	@Override
	public int getCurrentSize() {
		if (results != null)
			return results.size();
		return 0;
	}
}
