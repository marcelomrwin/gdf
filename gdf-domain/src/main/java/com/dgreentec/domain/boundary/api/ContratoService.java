package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface ContratoService {

	PagedList<Contrato> consultarContratos(Tenant tenant, FiltroContrato filtro);

	Contrato adicionarContrato(Tenant tenant, Contrato pContrato);

	Contrato atualizarContrato(Tenant tenant, Contrato pContrato);

	void removerContrato(Tenant tenant, Contrato pContrato);

	Contrato consultarContratoPorIdContrato(Tenant tenant, Long idContrato);

}
