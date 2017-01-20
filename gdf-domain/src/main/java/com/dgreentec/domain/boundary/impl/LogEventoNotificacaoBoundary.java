package com.dgreentec.domain.boundary.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.dgreentec.domain.boundary.api.LogEventoNotificacaoService;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.repository.LogEventoNotificacaoRepository;
import com.dgreentec.domain.repository.filter.FiltroLogEventoNotificacao;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.infrastructure.service.boundary.AbstractBoundary;

@Stateless
public class LogEventoNotificacaoBoundary extends AbstractBoundary implements LogEventoNotificacaoService {

	@Inject
	private LogEventoNotificacaoRepository logEventoNotificacaoRepository;

	public PagedList<LogEventoNotificacao> consultarLogEventoNotificacaos(Contrato contrato, FiltroLogEventoNotificacao filtro) {
		return logEventoNotificacaoRepository.consultar(filtro);
	}

	public LogEventoNotificacao adicionarLogEventoNotificacao(Contrato contrato, LogEventoNotificacao pLogEventoNotificacao) {
		return logEventoNotificacaoRepository.adicionar(pLogEventoNotificacao);
	}

	public LogEventoNotificacao atualizarLogEventoNotificacao(Contrato contrato, LogEventoNotificacao pLogEventoNotificacao) {
		return logEventoNotificacaoRepository.atualizar(pLogEventoNotificacao);
	}

	public void removerLogEventoNotificacao(Contrato contrato, LogEventoNotificacao pLogEventoNotificacao) {
		logEventoNotificacaoRepository.excluir(pLogEventoNotificacao);
	}

	public LogEventoNotificacao consultarLogEventoNotificacaoPorIdLogEvento(Contrato contrato, Long idLogEvento) {
		return logEventoNotificacaoRepository.consultarPorChave(idLogEvento);
	}

}
