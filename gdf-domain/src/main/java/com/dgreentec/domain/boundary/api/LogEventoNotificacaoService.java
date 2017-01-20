package com.dgreentec.domain.boundary.api;

import javax.ejb.Local;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.Contrato;
import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.repository.filter.FiltroLogEventoNotificacao;

@Local
public interface LogEventoNotificacaoService {

	PagedList<LogEventoNotificacao> consultarLogEventoNotificacaos(Contrato contrato, FiltroLogEventoNotificacao filtro);

	LogEventoNotificacao adicionarLogEventoNotificacao(Contrato contrato, LogEventoNotificacao pLogEventoNotificacao);

	LogEventoNotificacao atualizarLogEventoNotificacao(Contrato contrato, LogEventoNotificacao pLogEventoNotificacao);

	void removerLogEventoNotificacao(Contrato contrato, LogEventoNotificacao pLogEventoNotificacao);

	LogEventoNotificacao consultarLogEventoNotificacaoPorIdLogEvento(Contrato contrato, Long idLogEvento);

}
