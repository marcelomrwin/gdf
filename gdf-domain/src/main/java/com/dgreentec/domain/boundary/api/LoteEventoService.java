package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.filter.FiltroLoteEvento;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface LoteEventoService {

	PagedList<LoteEvento> consultarLoteEventos(Tenant tenant, FiltroLoteEvento filtro);

	LoteEvento adicionarLoteEvento(Tenant tenant, LoteEvento pLoteEvento);

	LoteEvento atualizarLoteEvento(Tenant tenant, LoteEvento pLoteEvento);

	void removerLoteEvento(Tenant tenant, LoteEvento pLoteEvento);

	LoteEvento consultarLoteEventoPorIdLoteEvento(Tenant tenant, Integer idLoteEvento);

}
