package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.dgreentec.domain.boundary.api.LoteEventoService;
import com.dgreentec.domain.model.LoteEvento;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.LoteEventoRepository;
import com.dgreentec.domain.repository.filter.FiltroLoteEvento;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class LoteEventoBoundary extends AbstractBoundary implements LoteEventoService {

	@Inject
	private LoteEventoRepository loteEventoRepository;

	public PagedList<LoteEvento> consultarLoteEventos(Tenant tenant, FiltroLoteEvento filtro) {
		return loteEventoRepository.consultar(filtro);
	}

	public LoteEvento adicionarLoteEvento(Tenant tenant, LoteEvento pLoteEvento) {
		return loteEventoRepository.adicionar(pLoteEvento);
	}

	public LoteEvento atualizarLoteEvento(Tenant tenant, LoteEvento pLoteEvento) {
		return loteEventoRepository.atualizar(pLoteEvento);
	}

	public void removerLoteEvento(Tenant tenant, LoteEvento pLoteEvento) {
		loteEventoRepository.excluir(pLoteEvento);
	}

	public LoteEvento consultarLoteEventoPorIdLoteEvento(Tenant tenant, Integer idLoteEvento) {
		return loteEventoRepository.consultarPorChave(idLoteEvento);
	}

}
