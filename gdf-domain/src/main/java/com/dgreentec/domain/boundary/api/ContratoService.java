package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface ContratoService {

	PagedList<Contrato> consultarContratos(FiltroContrato filtro);

	Contrato adicionarContrato(Contrato pContrato);

	Contrato atualizarContrato(Contrato pContrato);

	void removerContrato(Contrato pContrato);

	Contrato consultarContratoPorIdContrato(Long idContrato);

}
