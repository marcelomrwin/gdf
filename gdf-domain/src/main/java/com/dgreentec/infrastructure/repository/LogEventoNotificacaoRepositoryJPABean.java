package com.dgreentec.infrastructure.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.dgreentec.domain.model.LogEventoNotificacao;
import com.dgreentec.domain.repository.LogEventoNotificacaoRepository;
import com.dgreentec.domain.repository.filter.FiltroLogEventoNotificacao;
import com.dgreentec.infrastructure.persistence.GenericModelRepository;
import com.dgreentec.infrastructure.persistence.pagination.PageEntityList;
import com.dgreentec.infrastructure.persistence.pagination.PagedList;

@ApplicationScoped
public class LogEventoNotificacaoRepositoryJPABean extends GenericModelRepository<LogEventoNotificacao>
		implements LogEventoNotificacaoRepository {

	private static final long serialVersionUID = 8203578348632510594L;

	public PagedList<LogEventoNotificacao> consultar(FiltroLogEventoNotificacao filtro) {

		CriteriaBuilder cb = createCriteriaBuilder();
		CriteriaQuery<LogEventoNotificacao> cq = cb.createQuery(LogEventoNotificacao.class);
		Root<LogEventoNotificacao> root = cq.from(LogEventoNotificacao.class);
		cq.select(root);
// order
		configureOrder(cb, cq, root, filtro);
// filter
		filtro.configurarBusca(cb, cq, root);
// pagination
		TypedQuery<LogEventoNotificacao> tq = createTypedQuery(cq, filtro, true);

// count criteria
		CriteriaBuilder cbc = createCriteriaBuilder();
		CriteriaQuery<Long> qc = cbc.createQuery(Long.class);
		Root<LogEventoNotificacao> rootCount = qc.from(LogEventoNotificacao.class);
		qc.select(cbc.count(rootCount));
		filtro.configurarBusca(cbc, qc, rootCount);

		List<LogEventoNotificacao> resultList = tq.getResultList();
		Long count = processCountQuery(qc);

		return new PageEntityList<>(resultList, count);
	}
}
