package com.dgreentec.infrastructure.persistence;

import java.io.Serializable;

import com.dgreentec.infrastructure.model.DomainObject;
import com.dgreentec.infrastructure.persistence.filter.FiltroAbstrato;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

public interface ModelRepositoryJPA<T extends DomainObject> extends RepositoryJPA {

	T adicionar(T entity);

	T atualizar(T entity);

	T atualizar(T entity, boolean flush);

	void excluir(T entity);

	T consultarPorChave(Serializable key);

	T buscarPorChave(Serializable key);

	PagedList<T> listar(FiltroAbstrato<T> filtro);

}
