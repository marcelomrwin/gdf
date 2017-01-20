package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;

import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.model.Tenant;
import com.dgreentec.domain.repository.filter.FiltroLogEventoNotificacao;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@Local
public interface LogEventoNotificacaoService {

	PagedList<LogEventoNotificacao> consultarLogEventoNotificacaos(Tenant tenant, FiltroLogEventoNotificacao filtro);

	LogEventoNotificacao adicionarLogEventoNotificacao(Tenant tenant, LogEventoNotificacao pLogEventoNotificacao);

	LogEventoNotificacao atualizarLogEventoNotificacao(Tenant tenant, LogEventoNotificacao pLogEventoNotificacao);

	void removerLogEventoNotificacao(Tenant tenant, LogEventoNotificacao pLogEventoNotificacao);

	LogEventoNotificacao consultarLogEventoNotificacaoPorIdLogEvento(Tenant tenant, Long idLogEvento);

}
