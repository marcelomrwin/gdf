package com.dgreentec.domain.repository;

import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.repository.filter.FiltroLoteEvento;

public interface LoteEventoRepository extends ModelRepositoryJPA<LoteEvento> {

	PagedList<LoteEvento> consultar(FiltroLoteEvento filtro);

}
