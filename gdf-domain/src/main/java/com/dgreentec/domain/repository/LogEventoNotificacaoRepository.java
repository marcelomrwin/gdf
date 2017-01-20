package com.dgreentec.domain.repository;

import com.dgreentec.infrastructure.persistence.ModelRepositoryJPA;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;
import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.repository.filter.FiltroLogEventoNotificacao;

public interface LogEventoNotificacaoRepository extends ModelRepositoryJPA<LogEventoNotificacao> {

	PagedList<LogEventoNotificacao> consultar(FiltroLogEventoNotificacao filtro);

}
