package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.repository.filter.FiltroLoteEvento;

@Local
public interface LoteEventoService {

	PagedList<LoteEvento> consultarLoteEventos(Contrato contrato, FiltroLoteEvento filtro);

	LoteEvento adicionarLoteEvento(Contrato contrato, LoteEvento pLoteEvento);

	LoteEvento atualizarLoteEvento(Contrato contrato, LoteEvento pLoteEvento);

	void removerLoteEvento(Contrato contrato, LoteEvento pLoteEvento);

	LoteEvento consultarLoteEventoPorIdLoteEvento(Contrato contrato, Integer idLoteEvento);

}
