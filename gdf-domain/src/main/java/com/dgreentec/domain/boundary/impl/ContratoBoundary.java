package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.dgreentec.domain.boundary.api.ContratoService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.ContratoRepository;
import com.dgreentec.domain.repository.filter.FiltroContrato;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class ContratoBoundary extends AbstractBoundary implements ContratoService {

	@Inject
	private ContratoRepository contratoRepository;

	public PagedList<Contrato> consultarContratos(Tenant tenant, FiltroContrato filtro) {
		return contratoRepository.consultar(filtro);
	}

	public Contrato adicionarContrato(Tenant tenant,Contrato pContrato) {
		Contrato contrato = contratoRepository.adicionar(pContrato);
		return contrato;
	}

	public Contrato atualizarContrato(Tenant tenant,Contrato pContrato) {
		return contratoRepository.atualizar(pContrato);
	}

	public void removerContrato(Tenant tenant,Contrato pContrato) {
		contratoRepository.excluir(pContrato);
	}

	public Contrato consultarContratoPorIdContrato(Tenant tenant,Long idContrato) {
		return contratoRepository.consultarPorChave(idContrato);
	}

}
