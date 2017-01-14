package com.dgreentec.domain.repository;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

public interface ContratoRepository extends ModelRepositoryJPA<Contrato> {
	PagedList<Contrato> consultar(FiltroContrato filtro);
}
