package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import com.dgreentec.domain.boundary.api.LogEventoNotificacaoService;
import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.LogEventoNotificacaoRepository;
import com.dgreentec.domain.repository.filter.FiltroLogEventoNotificacao;
import com.dgreentec.infrastructure.interceptor.EJBTransactionInterceptor;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.repository.multitenant.TenantInterceptor;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
@Interceptors({ TenantInterceptor.class, EJBTransactionInterceptor.class })
public class LogEventoNotificacaoBoundary extends AbstractBoundary implements LogEventoNotificacaoService {

	@Inject
	private LogEventoNotificacaoRepository logEventoNotificacaoRepository;

	public PagedList<LogEventoNotificacao> consultarLogEventoNotificacaos(Tenant tenant, FiltroLogEventoNotificacao filtro) {
		return logEventoNotificacaoRepository.consultar(filtro);
	}

	public LogEventoNotificacao adicionarLogEventoNotificacao(Tenant tenant, LogEventoNotificacao pLogEventoNotificacao) {
		return logEventoNotificacaoRepository.adicionar(pLogEventoNotificacao);
	}

	public LogEventoNotificacao atualizarLogEventoNotificacao(Tenant tenant, LogEventoNotificacao pLogEventoNotificacao) {
		return logEventoNotificacaoRepository.atualizar(pLogEventoNotificacao);
	}

	public void removerLogEventoNotificacao(Tenant tenant, LogEventoNotificacao pLogEventoNotificacao) {
		logEventoNotificacaoRepository.excluir(pLogEventoNotificacao);
	}

	public LogEventoNotificacao consultarLogEventoNotificacaoPorIdLogEvento(Tenant tenant, Long idLogEvento) {
		return logEventoNotificacaoRepository.consultarPorChave(idLogEvento);
	}

}
